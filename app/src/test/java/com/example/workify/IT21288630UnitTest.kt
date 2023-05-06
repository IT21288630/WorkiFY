package com.example.workify

import com.google.common.truth.Truth.assertThat
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

        assertThat(result).isTrue()
    }

    @Test
    fun login_isIncorrect() {
        val email = "acds@gmail.com"
        val password = "dsa"

        val result = IT21288630TestClass.login(email, password)

        assertThat(result).isFalse()
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

        assertThat(result).isTrue()
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

        assertThat(result).isFalse()
    }

    @Test
    fun addingService_isCorrect() {
        val services = arrayOf("Electrical Help", "Plumbing Repairs", "Help Moving", "Yardwork &amp; Gardening", "Furniture Assembly", "Home Cleaning")
        val description = "Test description"
        val hrRate = "1000"

        val result = IT21288630TestClass.addAService(services[0], description, hrRate)

        assertThat(result).isTrue()
    }

    @Test
    fun addingService_isIncorrect() {
        val services = arrayOf("Electrical Help", "Plumbing Repairs", "Help Moving", "Yardwork &amp; Gardening", "Furniture Assembly", "Home Cleaning")
        val description = "Test description"
        val hrRate = "1000cds"

        val result = IT21288630TestClass.addAService(services[0], description, hrRate)

        assertThat(result).isFalse()
    }


}