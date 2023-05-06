package com.example.workify.testClasses

import com.example.workify.dataClasses.Order



class IT21256646TestClass {

    companion object {
        public fun DetailsEmptyCheck(
            CusName: String,
            CusAddress: String,
            CusPhone: String,
            CusTitle: String,
            CusDesc: String,
            CusDate: String
        ) : String {

            if (CusName.isEmpty()) {
                return "Please enter Name"
            }
            if (CusAddress.isEmpty()) {
                return "Please enter Address"
            }
            if (CusPhone.isEmpty()) {
                return "Please enter Phone"
            }
            if (CusTitle.isEmpty()) {
                return "Please enter Title"
            }
            if (CusDesc.isEmpty()) {
                return "Please enter Desc"
            }
            if (CusDate.isEmpty()) {
                return "Please enter Date"
            }

            val order = Order(CusName, CusAddress, CusPhone, CusTitle, CusDesc, CusDate)

            return "Success"
        }

        fun ValideData(CusPhone: String): Boolean{

            if (CusPhone.length != 10){
                return false
            }

            try {
                CusPhone.toInt()
            }catch (e: java.lang.Exception){
                return false
            }

            val order = Order(CusPhone)

            return true
        }

    }




}

