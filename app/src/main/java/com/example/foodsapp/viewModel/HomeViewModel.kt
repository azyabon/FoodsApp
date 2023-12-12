package com.example.foodsapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsapp.db.MealDatabase
import com.example.foodsapp.pojo.Category
import com.example.foodsapp.pojo.CategoryList
import com.example.foodsapp.pojo.MealsByCategoryList
import com.example.foodsapp.pojo.MealsByCategory
import com.example.foodsapp.pojo.Meal
import com.example.foodsapp.pojo.MealList
import com.example.foodsapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var savedMealsLiveData = mealDatabase.mealDao().getMeals()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal;
                } else {
                    Log.d("HomeFragment", "no random-meal data")
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularMeals() {
        RetrofitInstance.api.getPopularMeals("Seafood").enqueue(object: Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    popularMealsLiveData.value = response.body()!!.meals
                } else {
                    Log.d("HomeFragment", "no popular-meals data")
                    return
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("HomeFragment", t.message.toString())
            }
        })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeFragment", t.message.toString())
            }
        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularMealsLiveData(): LiveData<List<MealsByCategory>> {
        return popularMealsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeSavedMealsLiveData(): LiveData<List<Meal>> {
        return savedMealsLiveData
    }
}