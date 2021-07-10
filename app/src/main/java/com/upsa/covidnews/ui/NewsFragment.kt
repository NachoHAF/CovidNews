package com.upsa.covidnews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.upsa.covidnews.R
import com.upsa.covidnews.adapter.NewsAdapter
import com.upsa.covidnews.api.RetrofitClient
import com.upsa.covidnews.db.NewsDatabase
import com.upsa.covidnews.db.NewsRoom
import com.upsa.covidnews.model.NewsResponse
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_news, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setNewsToRecyclerView()
        swipe_refresh.setOnRefreshListener {
                updateNewsToRecyclerView()
            swipe_refresh.isRefreshing = false
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setNewsToRecyclerView(){
        val database =NewsDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            val allNews = database?.newsDao()?.getAllNews()
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = allNews?.let { NewsAdapter(it) }
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun updateNewsToRecyclerView(){

        RetrofitClient.instance2.getNews()
            .enqueue(object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    println(t.message)
                    print("No response")
                }

                override fun onResponse(
                    call: Call<NewsResponse>,

                    response: Response<NewsResponse>
                ) {
                    val allArticles = response.body()?.articles
                    val dbNews =NewsDatabase.getInstance(requireContext())
                    val newsDao = dbNews?.newsDao()

                    lifecycleScope.launch {
                        newsDao?.nukeNewsTable()

                        if (allArticles != null) {
                            for(element in allArticles) {

                                val oneNew = NewsRoom(
                                    0,
                                    element.title ,
                                    element.publishedAt,
                                    element.urlToImage,
                                    element.url
                                )
                                newsDao?.insert(oneNew)

                                setNewsToRecyclerView()
                            }
                        }
                    }
                }
            })
    }
}
