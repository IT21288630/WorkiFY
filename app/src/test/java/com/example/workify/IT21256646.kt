package com.example.workify

import com.example.workify.testClasses.IT21256646TestClass
import com.example.workify.testClasses.IT21288630TestClass
import org.junit.Assert
import org.junit.Test

class IT21256646 {

    @Test
    fun data_isCorrect() {

        val result = IT21256646TestClass.ValideData("1234567890")

        Assert.assertEquals(true, result)
    }

    @Test
    fun data_isInCorrect() {

        val result = IT21256646TestClass.ValideData("123456789011")

        Assert.assertEquals(false, result)
    }

    @Test
    fun data_isNotNull() {

        val result = IT21256646TestClass.DetailsEmptyCheck("Kasun","Kandy","0768515777","123","asdfg","1/03/2023")

        Assert.assertEquals("Success", result)
    }

    @Test
    fun data_isNull() {

        val result = IT21256646TestClass.DetailsEmptyCheck("Kasun","Kandy","","123","asdfg","1/03/2023")

        Assert.assertEquals("Please enter Phone", result)
    }

}