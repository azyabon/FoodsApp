package com.example.foodsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodsapp.adapters.MealAdapter
import com.example.foodsapp.databinding.ActivityCategoryBinding
import com.example.foodsapp.fragments.HomeFragment
import com.example.foodsapp.viewModel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        prepareRecycleView()

        categoryViewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]

        categoryViewModel.getCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = "${intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!}: ${mealsList.size}"
            mealAdapter.setMeals(mealsList)
        })
    }

    private fun prepareRecycleView() {
        mealAdapter = MealAdapter()

        binding.rvCategoryMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealAdapter
        }
    }
}