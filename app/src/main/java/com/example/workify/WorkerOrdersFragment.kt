package com.example.workify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 */
class WorkerOrdersFragment : Fragment(R.layout.fragment_worker_orders) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var workerOrderSelect: Spinner = view.findViewById(R.id.WorkerOrderSelect)
        val workerPendingOrdersFragment = WorkerPendingOrdersFragment()
        val workerAcceptedOrdersFragment = WorkerAcceptedOrdersFragment()

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", bundle!!.getString("curWorkerEmail"))



        workerPendingOrdersFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerOrders, workerPendingOrdersFragment)
            commit()
        }

        workerOrderSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position){
                    0 -> {
                        workerPendingOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flWorkerOrders, workerPendingOrdersFragment)
                            commit()
                        }
                    }

                    1 -> {
                        workerAcceptedOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flWorkerOrders, workerAcceptedOrdersFragment)
                            commit()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }
        }
    }


}