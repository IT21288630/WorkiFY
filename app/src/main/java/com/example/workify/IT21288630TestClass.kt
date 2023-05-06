package com.example.workify

import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Worker

class IT21288630TestClass {
    companion object{
        fun login(email: String, password: String): Boolean{
            return email == "asd@gmail.com" && password == "456dsa"
        }

        fun register(name: String, email: String, district: String, password: String, rePassword: String, description: String,  phone: String): Boolean{
            if(!email.lowercase().contains("@gmail.com")){
                return false
            }

            if (phone.length != 10){
                return false
            }

            if (rePassword !=password){
                return false
            }

            try {
                phone.toInt()
            }catch (e: java.lang.Exception){
                return false
            }

            val worker = Worker(name, email, district, password, description, null, null, phone)

            return true
        }

        fun addAService(serviceName: String, description: String, hrRate: String): Boolean{
            try {
                hrRate.toDouble()
            }catch (e: java.lang.Exception){
                return false
            }

            val category = Category(serviceName, description, null, hrRate)

            return true
        }
    }

}