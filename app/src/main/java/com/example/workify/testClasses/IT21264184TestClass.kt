package com.example.workify.testClasses

import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Customer
import com.example.workify.dataClasses.Worker
import org.jetbrains.annotations.TestOnly

class IT21264184TestClass {
    @TestOnly
    fun Cuslogin_isCorrect() {
        val email = "kamal@gmail.com"
        val password = "kamal23"

        val result = Cuslogin(email, password)

        assertThat(result).isTrue()
    }

    private fun assertThat(result: Boolean): Any {

        return result
    }

    companion object{


        fun Cuslogin(email: String, password: String): Boolean{
            return email == "Kamal@gmail.com" && password == "kamal123"
        }

        fun Cusregister(name: String, email: String, district: String, password: String): Boolean{
            if(!email.lowercase().contains("@gmail.com")){
                return false
            }

            val customer = Customer(name, email, district, password)

            return true
        }


}

private fun Any.isTrue() {
    TODO("Not yet implemented")
}
}
