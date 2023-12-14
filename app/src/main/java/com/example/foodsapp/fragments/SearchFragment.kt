package com.example.foodsapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodsapp.activities.MainActivity
import com.example.foodsapp.activities.MealActivity
import com.example.foodsapp.adapters.MealsAdapter
import com.example.foodsapp.databinding.FragmentSearchBinding
import com.example.foodsapp.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecycleViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()

        binding.imgSearchArrow.setOnClickListener { searchMeals() }

        observeSearchMealsLiveData()

        var searchJob: Job? = null

        binding.etSearch.addTextChangedListener {searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }

        onMealClick()
    }

    private fun onMealClick() {
        searchRecycleViewAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)

            startActivity(intent);
        }
    }

    private fun observeSearchMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
            searchRecycleViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.etSearch.text.toString()

        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecycleView() {
        searchRecycleViewAdapter = MealsAdapter()

        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecycleViewAdapter
        }
    }
}