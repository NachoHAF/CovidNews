package com.upsa.covidnews

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.upsa.covidnews.api.RetrofitClient
import com.upsa.covidnews.db.NewsDatabase
import com.upsa.covidnews.db.NewsRoom
import com.upsa.covidnews.model.NewsResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.item_container.*
import kotlinx.android.synthetic.main.settings_item.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Thread.sleep(2000)
        setTheme(R.style.Theme_CovidNews)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        //  bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNavigation, navController)


        //Mode Light/Dark SharedPreferences
        val appSettingPrefs : SharedPreferences = getSharedPreferences("AppSettingPrefs",0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode",false)

        if (isNightModeOn){AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // EN/ES  SharedPreferences
        val appSettingPrefsForLanguage : SharedPreferences = getSharedPreferences("AppSettingPrefsLanguage",0)
        val isEnglish : Int = appSettingPrefsForLanguage.getInt("Language",0)
        if (isEnglish == 0){
            val languageCode = "en"
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
            //val item: MenuItem = bottom_navigation.menu.findItem(R.id.statsFragment)
            //  item.title = getString(R.string.Symptoms)
        resources.updateConfiguration(config, resources.displayMetrics)

            bottomNavigation.menu[0].title = getString(R.string.naviStats)
            bottomNavigation.menu[1].title = getString(R.string.naviNews)
            bottomNavigation.menu[2].title = getString(R.string.naviHelp)


        }else{
              val languageCode = "es"
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

                bottomNavigation.menu[0].title = getString(R.string.naviEstadi)
                bottomNavigation.menu[1].title = getString(R.string.naviNoticias)
                bottomNavigation.menu[2].title = getString(R.string.naviAyuda)

        }



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.settingsFragment) {

                bottomNavigation.visibility = View.GONE
            } else {

                bottomNavigation.visibility = View.VISIBLE
            }
        }

        createNotificationChannel()
        lifecycleScope.launch {
            downloadWhenFirstRun()
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name : CharSequence = "covidNewsReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("covidNews",name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun downloadWhenFirstRun(){

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
                    val dbNews =NewsDatabase.getInstance(this@MainActivity)
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
                            }
                        }
                    }
                }
            })
    }
}