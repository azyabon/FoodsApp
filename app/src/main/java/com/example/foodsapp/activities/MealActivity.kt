package com.example.foodsapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.foodsapp.databinding.ActivityMealBinding
import com.example.foodsapp.db.MealDatabase
import com.example.foodsapp.fragments.HomeFragment
import com.example.foodsapp.pojo.Meal
import com.example.foodsapp.viewModel.MealViewModel
import com.example.foodsapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealYoutubeUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        setContentView(binding.root)

        getMealInfoFromIntent()
        setMealInfoInViews()

        loadingCase()
        mealMvvm.getMealById(mealId)
        observerMealLiveData()

        onYoutubeImageClick()
        onSaveClick()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onSaveClick() {
        binding.btnSave.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeUrl))

            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null
    private fun observerMealLiveData() {
        mealMvvm.observeMealLiveData().observe(this
        ) { meal ->
            onResponseCase()
            mealToSave = meal

            binding.tvCategory.text = "Category: ${meal!!.strCategory}"
            binding.tvPlace.text = "Area: ${meal!!.strArea}"
            binding.tvSteps.text = meal!!.strInstructions

            mealYoutubeUrl = meal.strYoutube ?: ""
        }
    }

    private fun setMealInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
    }

    private fun getMealInfoFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvPlace.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvPlace.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}