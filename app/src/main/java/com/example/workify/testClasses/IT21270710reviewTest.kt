package com.example.workify.testClasses

import com.example.workify.dataClasses.Review

class IT21270710reviewTest {
    public fun ValidateReview(Title:String, Description: String, Rec:String): Boolean {
        if(Title.length >= 25){
            return false
        }
        try {
            Title.toString()
        }catch (e: java.lang.Exception){
            return false
        }
        val review = Review(Title)

        return true
    }

    public fun EmptyCheckReviews(Title: String, Description:String,Recommend:String) : String {

        if(Title.isEmpty()){
            return "Please enter a valid title"
        }

        if(Description.isEmpty()){
            return "Please enter a valid Description"
        }

        val review = Review(Title, Description, Recommend)

        return "Success"

    }
}