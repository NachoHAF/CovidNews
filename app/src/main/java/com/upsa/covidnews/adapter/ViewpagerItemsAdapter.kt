package com.upsa.covidnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upsa.covidnews.R
import com.upsa.covidnews.model.ViewpagerItem

class ViewpagerItemsAdapter (private  val viewpagerItems: List<ViewpagerItem>):
    RecyclerView.Adapter<ViewpagerItemsAdapter.ViewpagerItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewpagerItemViewHolder {
        return ViewpagerItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewpagerItemViewHolder, position: Int) {
        holder.bind(viewpagerItems[position])
    }

    override fun getItemCount(): Int {
        return viewpagerItems.size
    }

    inner class ViewpagerItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val imageHelp = view.findViewById<ImageView>(R.id.imageHelp)
        private val textTitle = view.findViewById<TextView>(R.id.textTitle)
        private val textDescription = view.findViewById<TextView>(R.id.textDescription)

        fun bind(viewpagerItem: ViewpagerItem){
            imageHelp.setImageResource(viewpagerItem.viewpagerImage)
            textTitle.text = viewpagerItem.title
            textDescription.text =viewpagerItem.description
        }
    }
}