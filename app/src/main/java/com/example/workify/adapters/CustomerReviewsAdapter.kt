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
import com.example.workify.activities.ViewFullReviewActivity
import com.example.workify.dataClasses.CustomerReview

class CustomerReviewsAdapter(
    private var data : List<CustomerReview>,
    private var context: Context
    ) :

    RecyclerView.Adapter<CustomerReviewsAdapter.ViewHolder>(){


    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val sellers_review_title_on_cus_profile: TextView
        val sellers_review_description_on_cus_profile: TextView
      //  val sellers_review_star_on_cus_profile: TextView
        val sellers_review_name_on_cus_profile: TextView


        val ShowreviewcutomerViewBtn: Button
        val ShowreviewcutomerEditBtn: Button
        val ShowreviewcutomerDeleteBtn: Button


        init {
            sellers_review_name_on_cus_profile = view.findViewById(R.id.sellers_review_name_on_cus_profile)
            sellers_review_title_on_cus_profile = view.findViewById(R.id.sellers_review_title_on_cus_profile)
            sellers_review_description_on_cus_profile = view.findViewById(R.id.sellers_review_description_on_cus_profile)
//            sellers_review_star_on_cus_profile = view.findViewById(R.id.sellers_review_star_on_sel_profile)

            ShowreviewcutomerViewBtn = view.findViewById(R.id.ShowreviewcutomerViewBtn)
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

        holder.ShowreviewcutomerViewBtn.setOnClickListener {

            var intent = Intent(context, ViewFullReviewActivity::class.java)
            intent.putExtra("customer_email", data[position].customer_email)
            intent.putExtra("rev_ID", data[position].rev_ID)
            context.startActivity(intent)

        }

        holder.ShowreviewcutomerEditBtn.setOnClickListener {

            var intent = Intent(context, CustomerEditReviewActivity::class.java)
            intent.putExtra("customer_email", data[position].customer_email)
            intent.putExtra("rev_ID", data[position].rev_ID)
            context.startActivity(intent)

            println("This is the id in adapter: " + data[position].rev_ID)
        }

        holder.ShowreviewcutomerDeleteBtn.setOnClickListener {
            var intent = Intent(context, CustomerEditReviewActivity::class.java)
            intent.putExtra("customer_email", data[position].customer_email)
            intent.putExtra("rev_ID", data[position].rev_ID)
            context.startActivity(intent)
        }


    }

    fun setData(data: List<CustomerReview>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }


}