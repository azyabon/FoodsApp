package com.example.foodsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.databinding.CategoryItemBinding
import com.example.foodsapp.pojo.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categoryList:List<Category> = ArrayList()
    var onItemClick: ((Category) -> Unit)? = null

    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    class CategoryViewHolder(val binding:CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            tvCategoryName.text = categoryList[position].strCategory

            Glide.with(holder.itemView)
                .load(categoryList[position].strCategoryThumb)
                .into(imgCategory)
        }

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}