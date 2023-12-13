package com.example.foodsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.activities.CategoryActivity
import com.example.foodsapp.activities.MainActivity
import com.example.foodsapp.activities.MealActivity
import com.example.foodsapp.adapters.CategoryAdapter
import com.example.foodsapp.adapters.PopularMealAdapter
import com.example.foodsapp.databinding.FragmentHomeBinding
import com.example.foodsapp.fragments.sheets.MealSheet
import com.example.foodsapp.pojo.MealsByCategory
import com.example.foodsapp.pojo.Meal
import com.example.foodsapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealsAdapter: PopularMealAdapter
    private lateinit var categoriesAdapter: CategoryAdapter

    companion object {
        const val MEAL_ID = "com.example.foodsapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodsapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodsapp.fragments.thumbMeal"

        const val CATEGORY_NAME = "com.example.foodsapp.fragments.strCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        popularMealsAdapter = PopularMealAdapter()
        categoriesAdapter = CategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularMealsRecycleView()
        prepareCategoriesRecycleView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularMeals()
        observerPopularMeals()
        onPopularItemClick()

        viewModel.getCategories()
        observerCategories()
        onCategoryClick()

        onPopularLongItemClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun onPopularLongItemClick() {
        popularMealsAdapter.onLongItemClick = {meal ->
            val mealSheetFragment: MealSheet = MealSheet.newInstance(meal.idMeal)

            mealSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)

            startActivity(intent);
        }
    }

    private fun prepareCategoriesRecycleView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularMealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)

            startActivity(intent);
        }
    }

    private fun preparePopularMealsRecycleView() {
        binding.rvMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealsAdapter
        }
    }

    private fun observerPopularMeals() {
        viewModel.observePopularMealsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularMealsAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

            startActivity(intent);
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }
}