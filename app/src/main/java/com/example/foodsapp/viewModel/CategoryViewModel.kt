package com.example.foodsapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodsapp.pojo.MealsByCategory
import com.example.foodsapp.pojo.MealsByCategoryList
import com.example.foodsapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel: ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getCategory(category: String) {
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object: Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealsList ->
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryViewModel", t.message.toString())
            }

        })
    }

    fun observeMealsLiveData(): LiveData<List<MealsByCategory>> {
        return mealsLiveData
    }
}