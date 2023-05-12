package com.example.workify.testClasses

import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Worker

class IT21288630TestClass {
    companion object{
        fun login(email: String, password: String): Boolean{
            return email == "asd@gmail.com" && password == "456dsa"
        }

        fun register(name: String, email: String, district: String, password: String, rePassword: String, description: String,  phone: String): String{
            if (name.isEmpty()){
                return "name"
            }

            if(email.isEmpty()){
                return "emailEmp"
            }

            if (phone.isEmpty()){
                return "phoneEmp"
            }

            if (district.isEmpty()){
                return "disc"
            }

            if (description.isEmpty()){
                return "desc"
            }

            if (password.isEmpty()){
                return "password"
            }

            if (rePassword.isEmpty()){
                return "repassEmp"
            }

            if(!email.lowercase().contains("@gmail.com")){
                return "email"
            }

            if (phone.length != 10){
                return "phone"
            }

            if (rePassword !=password){
                return "repass"
            }

            try {
                phone.toInt()
            }catch (e: java.lang.Exception){
                return "phone"
            }

            return "Success"
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