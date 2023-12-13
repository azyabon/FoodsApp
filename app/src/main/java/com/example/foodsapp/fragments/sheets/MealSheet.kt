package com.example.foodsapp.fragments.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodsapp.activities.MainActivity
import com.example.foodsapp.databinding.FragmentMealSheetBinding
import com.example.foodsapp.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealSheet : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealSheetBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }

        observeSheetMeal()
    }

    private fun observeSheetMeal() {
        viewModel.observeSheetMealLiveData().observe(viewLifecycleOwner, Observer {meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgMealSheet)

            binding.tvMealSheetPlace.text = meal.strArea
            binding.tvMealSheetCategory.text = meal.strCategory
            binding.tvMealSheetName.text = meal.strMeal
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealSheet().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}