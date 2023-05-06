package com.example.workify.testClasses

import com.example.workify.dataClasses.Review

class IT21270710reviewTest {

    companion object{
        public fun reviewDetails(
            Title: String,
            Description: String,
            Recommend: String,
            Star:Int
        ) : String{

            if(Title.isEmpty()){
                return "Please Enter a valid Title"
            }
            if(Description.isEmpty()){
                return "Please Enter a valid description"
            }
            val starcount = Star.toString()
            if(starcount.isEmpty()){
                return "Please add a star count"
            }
            if(Recommend.isEmpty()){
                return "Would you like to recommend this seller to anyone?"
            }

            val Review = Review(Title, Description, Recommend, Star)

            return "Success"
        }

    }
    public fun ValidateReview(Title:String, Description: String, Recommend: String, Star: Int): Boolean {
        if(Title.length >= 25){
            return false
        }
        try {
            Title.toString()
        }catch (e: java.lang.Exception){
            return false
        }

        if(Description.length >= 100){
            return false
        }

        try {
            Description.toString()
        }catch (e: java.lang.Exception){
            return false
        }
        Recommend.lowercase()
        if(Recommend != "yes" || Recommend != "no"){
            return false
        }

        try {
            Star.toDouble()
        }catch (e: java.lang.Exception){
            return false
        }

        val review = Review(Title, Description, Recommend,Star)

        return true
    }

    public fun EmptyCheckReviews(Title: String, Description:String,Recommend:String, Star: Int) : String {

        if(Title.isEmpty()){
            return "Please enter a valid title"
        }

        if(Description.isEmpty()){
            return "Please enter a valid Description"
        }

        if(Recommend.isEmpty()){
            return "Please Select your option about seller recommendation"
        }

        if(Star <= 0){
            return "Please add stars to sumbit your review"

        }

        val review = Review(Title, Description, Recommend, Star)

        return "Success"

    }
}