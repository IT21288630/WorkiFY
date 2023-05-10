package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.CustomerReviewsForWorkerProfileAdapter
import com.example.workify.adapters.HomeSearchAdapter
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class HomeSearchFragment : Fragment(R.layout.fragment_home_search) {

    private val workerCatCollectionRef = Firebase.firestore.collection("worker_cat")
    private val workerCollectionRef = Firebase.firestore.collection("workers")
    private var serviceEmpty = false
    private var nameEmpty = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSearchServiceName = view.findViewById<TextView>(R.id.tvSearchServiceName)
        val spSearchFilterSelect = view.findViewById<Spinner>(R.id.spSearchFilterSelect)
        val ivSearchRemoveService = view.findViewById<ImageView>(R.id.ivSearchRemoveService)
        val etSearchNameInput = view.findViewById<EditText>(R.id.etSearchNameInput)
        val lyServiceFilter = view.findViewById<LinearLayout>(R.id.lyServiceFilter)

        val bundle = arguments
        var curService: String? = bundle!!.getString("curService")
        var serviceNameFromHome: String? = bundle!!.getString("serviceNameFromHome")

        tvSearchServiceName.text = curService

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHomeSearchResult)

        if(serviceNameFromHome != null){
            searchByServiceName(recyclerView, serviceNameFromHome, etSearchNameInput)
        }

        curService?.let { initSearch(it, recyclerView) }

        spSearchFilterSelect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {

                    when (position) {
                        1 -> {
                            val filter = "avgRatings"
                            curService?.let {
                                filteredSearch(
                                    it,
                                    recyclerView,
                                    filter,
                                    etSearchNameInput
                                )
                            }

                        }

                        2 -> {
                            val filter = "lowToHigh"
                            curService?.let {
                                filteredSearch(
                                    it,
                                    recyclerView,
                                    filter,
                                    etSearchNameInput
                                )
                            }

                        }
                        3 -> {
                            val filter = "highToLow"
                            curService?.let {
                                filteredSearch(
                                    it,
                                    recyclerView,
                                    filter,
                                    etSearchNameInput
                                )
                            }

                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    TODO("Not yet implemented")
                }

            }

        ivSearchRemoveService.setOnClickListener {
            noServiceSearch(recyclerView)
            lyServiceFilter.visibility = View.INVISIBLE
        }

        etSearchNameInput.addTextChangedListener {
            nameEmpty = false

            if (curService != null) {
                searchByName(recyclerView, etSearchNameInput.text.toString(), curService)
            }

            if(etSearchNameInput.text.toString().isEmpty()){
                nameEmpty = true
            }
        }
    }

    fun initSearch(curService: String, recyclerView: RecyclerView) {
        var workers = mutableListOf<Worker>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    workerCatCollectionRef.whereEqualTo("cName", curService).get().await()

                for (document in querySnapshot.documents) {
                    val querySnapshot2 =
                        workerCollectionRef.whereEqualTo("email", document.get("wEmail")).get()
                            .await()

                    for (document2 in querySnapshot2.documents) {
                        val worker = document2.toObject<Worker>()
                        worker?.price = document.get("hrRate") as String?

                        if (worker != null) {
                            workers.add(worker)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { HomeSearchAdapter(workers, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(workers, it) }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun filteredSearch(
        curService: String,
        recyclerView: RecyclerView,
        filter: String,
        etSearchNameInput: EditText
    ) {
        var workers = mutableListOf<Worker>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                var querySnapshot: QuerySnapshot?

                if (!serviceEmpty) {
                    querySnapshot =
                        workerCatCollectionRef.whereEqualTo("cName", curService).get().await()

                    when (filter) {
                        "lowToHigh" -> {
                            querySnapshot = workerCatCollectionRef.whereEqualTo("cName", curService)
                                .orderBy("hrRate", Query.Direction.ASCENDING).get().await()
                        }

                        "highToLow" -> {
                            querySnapshot = workerCatCollectionRef.whereEqualTo("cName", curService)
                                .orderBy("hrRate", Query.Direction.DESCENDING).get().await()
                        }
                    }
                } else {
                    querySnapshot = workerCatCollectionRef.get().await()

                    when (filter) {
                        "lowToHigh" -> {
                            querySnapshot = workerCatCollectionRef
                                .orderBy("hrRate", Query.Direction.ASCENDING).get().await()
                        }

                        "highToLow" -> {
                            querySnapshot = workerCatCollectionRef
                                .orderBy("hrRate", Query.Direction.DESCENDING).get().await()
                        }
                    }
                }

                for (document in querySnapshot?.documents!!) {
                    var querySnapshot2: QuerySnapshot? = null

                    querySnapshot2 = if (nameEmpty) {
                        workerCollectionRef.whereEqualTo("email", document.get("wEmail")).get()
                            .await()
                    } else {
                        workerCollectionRef.whereEqualTo("email", document.get("wEmail"))
                            .whereEqualTo("name", etSearchNameInput.text.toString()).get()
                            .await()
                    }

                    for (document2 in querySnapshot2.documents) {
                        val worker = document2.toObject<Worker>()
                        worker?.price = document.get("hrRate") as String?

                        if (worker != null) {
                            workers.add(worker)
                        }
                    }
                }

                if (filter == "avgRatings") {
                    workers.sortByDescending {
                        it.avgRating
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.context?.let { HomeSearchAdapter(workers, it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(workers, it) }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun noServiceSearch(recyclerView: RecyclerView) {
        var workers = mutableListOf<Worker>()
        serviceEmpty = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    workerCatCollectionRef.get().await()

                for (document in querySnapshot.documents) {
                    val querySnapshot2 =
                        workerCollectionRef.whereEqualTo("email", document.get("wEmail")).get()
                            .await()

                    for (document2 in querySnapshot2.documents) {
                        val worker = document2.toObject<Worker>()
                        worker?.price = document.get("hrRate") as String?

                        if (worker != null) {
                            workers.add(worker)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { HomeSearchAdapter(workers, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(workers, it) }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun searchByName(recyclerView: RecyclerView, name: String, curService: String) {
        var workers = mutableListOf<Worker>()

        CoroutineScope(Dispatchers.IO).launch {
            try {

                var querySnapshot: QuerySnapshot? = if (!serviceEmpty) {
                    workerCatCollectionRef.whereEqualTo("cName", curService).get().await()
                } else {
                    workerCatCollectionRef.get().await()
                }

                for (document in querySnapshot?.documents!!) {
                    val querySnapshot2 =
                        workerCollectionRef.whereEqualTo("email", document.get("wEmail")).get()
                            .await()

                    for (document2 in querySnapshot2.documents) {
                        val worker = document2.toObject<Worker>()
                        worker?.price = document.get("hrRate") as String?

                        if (worker != null) {
                            if(worker.name.lowercase().contains(name, ignoreCase = true)){
                                workers.add(worker)
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { HomeSearchAdapter(workers, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(workers, it) }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun searchByServiceName(recyclerView: RecyclerView, sName: String, etSearchNameInput: EditText) {
        var workers = mutableListOf<Worker>()

        CoroutineScope(Dispatchers.IO).launch {
            try {

                var querySnapshot = workerCatCollectionRef.get().await()

                for (document in querySnapshot.documents) {

                    if (document.get("cName").toString().lowercase().contains(sName, ignoreCase = true)){
                        val querySnapshot2 =
                            workerCollectionRef.whereEqualTo("email", document.get("wEmail")).get()
                                .await()

                        for (document2 in querySnapshot2.documents) {
                            val worker = document2.toObject<Worker>()
                            worker?.price = document.get("hrRate") as String?

                            if (worker != null) {
                                if (!nameEmpty) {
                                    if(worker.name.lowercase().contains(etSearchNameInput.text.toString(), ignoreCase = true)){
                                        workers.add(worker)
                                    }
                                }
                                else{
                                    workers.add(worker)
                                }
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = view?.let { HomeSearchAdapter(workers, it.context) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view?.context)
                    view?.context?.let { adapter?.setData(workers, it) }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}