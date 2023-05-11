package com.example.workify.adapters

// For Worker
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.CustomerEditReviewActivity
import com.example.workify.dataClasses.CustomerReview
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerReviewsAdapter(
    private var data : List<CustomerReview>,
    private var context: Context
    ) :

    RecyclerView.Adapter<CustomerReviewsAdapter.ViewHolder>(){

    private val revCollectionRef = Firebase.firestore.collection("customer_reviews")
    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val sellers_review_title_on_cus_profile: TextView
        val sellers_review_description_on_cus_profile: TextView
      //  val sellers_review_star_on_cus_profile: TextView
        val sellers_review_name_on_cus_profile: TextView



        val ShowreviewcutomerEditBtn: Button
        val ShowreviewcutomerDeleteBtn: Button


        init {
            sellers_review_name_on_cus_profile = view.findViewById(R.id.sellers_review_name_on_cus_profile)
            sellers_review_title_on_cus_profile = view.findViewById(R.id.sellers_review_title_on_cus_profile)
            sellers_review_description_on_cus_profile = view.findViewById(R.id.sellers_review_description_on_cus_profile)
//            sellers_review_star_on_cus_profile = view.findViewById(R.id.sellers_review_star_on_sel_profile)


            ShowreviewcutomerEditBtn = view.findViewById(R.id.ShowreviewcutomerEditBtn)
            ShowreviewcutomerDeleteBtn= view.findViewById(R.id.ShowreviewcutomerDeleteBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_show_reviews_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.sellers_review_name_on_cus_profile.text = data[position].rev_ID
        holder.sellers_review_title_on_cus_profile.text = data[position].title
        holder.sellers_review_description_on_cus_profile.text = data[position].description
        //holder.sellers_review_star_on_cus_profile.text = data[position].stars.toString()


        holder.ShowreviewcutomerEditBtn.setOnClickListener {

            var intent = Intent(context, CustomerEditReviewActivity::class.java)
            intent.putExtra("customer_email", data[position].customer_email)
            intent.putExtra("rev_ID", data[position].rev_ID)
            context.startActivity(intent)

            println("This is the id in adapter: " + data[position].rev_ID)
        }

        holder.ShowreviewcutomerDeleteBtn.setOnClickListener {


            CoroutineScope(Dispatchers.IO).launch {
                var reviews = mutableListOf<CustomerReview>()
                try {
                    val querySnapshot = revCollectionRef
                        .whereEqualTo("rev_ID",data[position].rev_ID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        revCollectionRef.document(document.id).delete()
                    }

                    val querySnapshot2 = revCollectionRef
                        .whereEqualTo("customer_email",  data[position].customer_email)
                        .whereEqualTo("rev_ID", data[position].rev_ID)
                        .get()
                        .await()

                    for (document2 in querySnapshot2.documents){
                        var review = document2.toObject<CustomerReview>()

                        if (review != null) {
                            reviews.add(review)
                        }
                    }

                    withContext(Dispatchers.Main){

                        setData(reviews, context)
                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }


    }

    fun setData(data: List<CustomerReview>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }


}