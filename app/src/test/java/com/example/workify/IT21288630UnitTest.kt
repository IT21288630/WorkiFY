package com.example.workify

import com.example.workify.testClasses.IT21288630TestClass
import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Test

/**
 * Local unit test, which will execute on the development machine (host).
 *
 */
class IT21288630UnitTest {
    @Test
    fun login_isCorrect() {
        val email = "asd@gmail.com"
        val password = "456dsa"

        val result = IT21288630TestClass.login(email, password)

        Assert.assertEquals(true, result)
    }

    @Test
    fun login_isIncorrect() {
        val email = "acds@gmail.com"
        val password = "dsa"

        val result = IT21288630TestClass.login(email, password)

        Assert.assertEquals(false, result)
    }

    @Test
    fun register_isCorrect() {
        val name = "JJ"
        val email = "jj@gmail.com"
        val district = "Kandy"
        val password = "sca155"
        val rePassword = "sca155"
        val description = "Test description"
        val phone = "0747894561"

        val result = IT21288630TestClass.register(email, email, district, password, rePassword, description, phone)

        Assert.assertEquals("Success", result)
    }

    @Test
    fun register_isIncorrect() {
        val name = "JJ"
        val email = "jj@gmail.com"
        val district = "Kandy"
        val password = "sca155"
        val rePassword = "sca155"
        val description = "Test description"
        val phone = "0747894sd1"

        val result = IT21288630TestClass.register(email, email, district, password, rePassword, description, phone)

        Assert.assertEquals("phone", result)
    }

    @Test
    fun addingService_isCorrect() {
        val services = arrayOf("Electrical Help", "Plumbing Repairs", "Help Moving", "Yardwork &amp; Gardening", "Furniture Assembly", "Home Cleaning")
        val description = "Test description"
        val hrRate = "1000"

        val result = IT21288630TestClass.addAService(services[0], description, hrRate)

        Assert.assertEquals(true, result)
    }

    @Test
    fun addingService_isIncorrect() {
        val services = arrayOf("Electrical Help", "Plumbing Repairs", "Help Moving", "Yardwork &amp; Gardening", "Furniture Assembly", "Home Cleaning")
        val description = "Test description"
        val hrRate = "1000cds"

        val result = IT21288630TestClass.addAService(services[0], description, hrRate)

        Assert.assertEquals(false, result)
    }


}