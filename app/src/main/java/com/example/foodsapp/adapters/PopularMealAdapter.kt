package com.example.foodsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.foodsapp.databinding.PopularItemBinding
import com.example.foodsapp.pojo.MealsByCategory

class PopularMealAdapter(): RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {
    lateinit var onItemClick: ((MealsByCategory) -> Unit)

    private var mealList = ArrayList<MealsByCategory>()

    fun setMeals(mealList: ArrayList<MealsByCategory>) {
        this.mealList = mealList

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMeal)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
    }

    class PopularMealViewHolder(val binding: PopularItemBinding): ViewHolder(binding.root)
}