package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.workify.R

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
        val email = bundle!!.getString("curCusEmail")

        val mBundle = Bundle()
        mBundle.putString("curCusEmail", email)



        customerPendingOrdersFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flCusOrders, customerPendingOrdersFragment)
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
                            replace(R.id.flCusOrders, customerPendingOrdersFragment)
                            commit()
                        }
                    }

                    1 -> {
                        customerAcceptedOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flCusOrders, customerAcceptedOrdersFragment)
                            commit()
                        }
                    }

                    2 -> {
                        customerCompletedOrdersFragment.arguments = mBundle
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.flCusOrders,customerCompletedOrdersFragment)
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