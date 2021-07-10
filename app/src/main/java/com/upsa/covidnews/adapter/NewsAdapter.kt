package com.upsa.covidnews.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.kpstv.cwt.CWT
import com.squareup.picasso.Picasso
import com.upsa.covidnews.R
import com.upsa.covidnews.db.NewsRoom
import kotlinx.android.synthetic.main.card_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewsAdapter(private val allNews: List < NewsRoom > ): RecyclerView.Adapter < NewsAdapter.ViewHolder > () {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var itemImage: ImageView = itemView.item_image
        var itemTitle: TextView = itemView.item_title
        var itemTime: TextView = itemView.item_time

        init {
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val detailsURL = allNews[position].url
                CWT.Builder(itemView.context).launch(detailsURL.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = allNews[position].title

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getDefault()
        val time = sdf.parse(allNews[position].publishedAt).time
        holder.itemTime.text = TimeAgo.using(time)

        Picasso.get().load(allNews[position].urlToImage).fit().centerCrop().placeholder(R.drawable.ic_no_image).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return allNews.size
    }
}