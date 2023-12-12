package com.example.foodsapp.retrofit

import com.example.foodsapp.pojo.CategoryList
import com.example.foodsapp.pojo.MealsByCategoryList
import com.example.foodsapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealById(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularMeals(@Query("c") category: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealsByCategoryList>
}