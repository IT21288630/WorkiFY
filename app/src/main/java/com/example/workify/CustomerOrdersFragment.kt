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
class CustomerOrdersFragment : Fragment(R.layout.fragment_customer_orders) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var customerOrderSelect: Spinner = view.findViewById(R.id.CustomerOrderSelect)
        val customerPendingOrdersFragment = CustomerPendingOrdersFragment()
        val customerAcceptedOrdersFragment = CustomerAcceptedOrdersFragment()
        val customerCompletedOrdersFragment = CustomerCompletedOrdersFragment()

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", bundle!!.getString("curWorkerEmail"))



        customerPendingOrdersFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerOrders, customerPendingOrdersFragment)
            commit()
        }

        customerOrderSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position){
                    0 -> {
                        customerPendingOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flWorkerOrders, customerPendingOrdersFragment)
                            commit()
                        }
                    }

                    1 -> {
                        customerAcceptedOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flWorkerOrders, customerAcceptedOrdersFragment)
                            commit()
                        }
                    }

                    2 -> {
                        customerCompletedOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flWorkerOrders,customerCompletedOrdersFragment)
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