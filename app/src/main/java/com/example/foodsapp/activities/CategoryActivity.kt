package com.example.foodsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodsapp.adapters.CategoryMealAdapter
import com.example.foodsapp.databinding.ActivityCategoryBinding
import com.example.foodsapp.fragments.HomeFragment
import com.example.foodsapp.viewModel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        prepareRecycleView()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        categoryViewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]

        categoryViewModel.getCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = "${intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!}: ${mealsList.size}"
            categoryMealAdapter.setMeals(mealsList)
        })

        onMealClick()
    }
    private fun onMealClick() {
        categoryMealAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent);
        }
    }


    private fun prepareRecycleView() {
        categoryMealAdapter = CategoryMealAdapter()

        binding.rvCategoryMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}