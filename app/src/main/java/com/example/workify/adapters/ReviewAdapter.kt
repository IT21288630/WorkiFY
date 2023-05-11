package com.example.workify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ReviewAdapter(private val reviews: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    // Get a reference to the Firestore database
    private val db = FirebaseFirestore.getInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       val ratingTextView: TextView = view.findViewById(R.id.sellers_review_star_on_sel_profile)
        val titleTextView: TextView = view.findViewById(R.id.sellers_review_title_on_cus_profile)
        val descriptionTextView: TextView = view.findViewById(R.id.sellers_review_description_on_cus_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_show_reviews_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]

        // Get a reference to the "reviews" collection in the database
        val reviewsRef = db.collection("reviews")

        // Query the "reviews" collection for documents with matching title and description
        reviewsRef.whereEqualTo("title", review.title)
            .whereEqualTo("description", review.description)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document in querySnapshot) {
                    // Set the rating of the review from the retrieved data
                    holder.ratingTextView.text = document.getDouble("stars").toString()
                }
            }
            .addOnFailureListener { exception: Exception ->
                // Handle errors here
            }

        // Set the title and description of the review
        holder.titleTextView.text = review.title
        holder.descriptionTextView.text = review.description
    }

    override fun getItemCount() = reviews.size
}
