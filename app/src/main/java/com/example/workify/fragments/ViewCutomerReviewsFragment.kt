package com.example.workify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.workify.R

/**
 * A simple [Fragment] subclass.
 * Use the [viewCutomerReviewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class viewCutomerReviewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(com.example.workify.ARG_PARAM1)
            param2 = it.getString(com.example.workify.ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cutomer_reviews, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment viewCutomerReviewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            viewCutomerReviewsFragment().apply {
                arguments = Bundle().apply {
                    putString(com.example.workify.ARG_PARAM1, param1)
                    putString(com.example.workify.ARG_PARAM2, param2)
                }
            }
    }
}