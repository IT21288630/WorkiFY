package com.example.workify.adapters

// For Worker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.Review

class CustomerReviewsforWorker(private val userList : ArrayList<Review>) : RecyclerView.Adapter<CustomerReviewsforWorker.myViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerReviewsforWorker.myViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_worker_profile_reviews, parent, false)
        return CustomerReviewsforWorker.myViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: CustomerReviewsforWorker.myViewHolder,
        position: Int
    ) {
        val currentItem = userList[position]

        holder.name.text = currentItem.worker_email
        holder.revtitle.text = currentItem.title
        holder.revstar.text = currentItem.stars.toString()

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.sellers_review_name_on_Sel_profile)
        val revtitle : TextView = itemView.findViewById(R.id.sellers_review_title_on_Sel_profile)
        //val revdescription : TextView = itemView.findViewById(R.id.tvRCName)
        val revstar : TextView = itemView.findViewById(R.id.sellers_review_star_on_sel_profile)
        //val revrecommend : TextView = itemView.findViewById(R.id.tvRCName)


    }


}