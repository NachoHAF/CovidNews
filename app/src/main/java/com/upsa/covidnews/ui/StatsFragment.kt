package com.upsa.covidnews.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.upsa.covidnews.R
import com.upsa.covidnews.api.RetrofitClient
import com.upsa.covidnews.model.SpainResponse
import com.upsa.covidnews.model.WorldResponse
import com.upsa.covidnews.service.NotificationReceiver
import kotlinx.android.synthetic.main.fragment_stats.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*


class StatsFragment: Fragment (){
    private lateinit var alarmManager : AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_stats, container, false)
        val spinnerCountries: Spinner = view.findViewById(R.id.spinner)

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.countries_spinner,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCountries.adapter = adapter
            }
        }


        val spinnerPrefs : SharedPreferences = requireActivity().getSharedPreferences("SpinnerPrefs",0)
        val spinnerPrefsEdit: SharedPreferences.Editor = spinnerPrefs.edit()

        when (spinnerPrefs.getInt("active_country_spinner", 0)) {
            0 -> {
                showSpain()
                spinnerCountries.setSelection(0)
            }

            1 -> {
                showAll()
                spinnerCountries.setSelection(1)
            }
            else -> {
                print("not recognized country!")
            }
        }


        spinnerCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                val country = parent?.getItemAtPosition(position).toString()
                if (country == resources.getStringArray(R.array.countries_spinner)[0] ){
                    spinnerPrefsEdit.putInt("active_country_spinner", 0)
                    spinnerPrefsEdit.apply()
                    print("show Spain")
                    showSpain()

                }
                if(country == resources.getStringArray(R.array.countries_spinner)[1] ){
                    spinnerPrefsEdit.putInt("active_country_spinner", 1)
                    spinnerPrefsEdit.apply()
                    print("show World")
                    showAll()
                }
            }
        }

     /*   //Spinner SharedPreferences
        val isSpain : Int = spinnerPrefs.getInt("active_country_spinner",0)
        if (isSpain == 0){
            showSpain()
        }else{
            showAll()
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }})


        reminder.setOnClickListener {
            activeNotification()
        }

        val alarmUp = PendingIntent.getBroadcast(
            context, 0,Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null

        if (alarmUp) {
            Log.d("myTag", "Alarm is already active")
             reminder.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_notifications_active))
        }else{
            Log.d("myTag","Alarm not activated")
        }
    }

    private fun showSpain(){
        RetrofitClient.instance.getSpain().enqueue(object : Callback<SpainResponse> {
            override fun onFailure(call: Call<SpainResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<SpainResponse>,
                response: Response<SpainResponse>
            ) {
                val spain = response.body()
                val confirmed = spain?.cases
                val deceased = spain?.deaths
                val recover = spain?.recovered
                val hospitalized = spain?.active
                val dangerous = spain?.critical

                val df = DecimalFormat(",###")

                totalConfirmed?.text = df.format(confirmed)
                totalDeceased?.text = df.format(deceased)
                recovered?.text = df.format(recover)
                active?.text = df.format(hospitalized)
                critical?.text = df.format(dangerous)
            }

        })
    }

    private fun showAll(){
        RetrofitClient.instance.getAll().enqueue(object : Callback<WorldResponse> {
            override fun onFailure(call: Call<WorldResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<WorldResponse>,
                response: Response<WorldResponse>
            ) {
                val world = response.body()
                val confirmed = world?.cases
                val deceased = world?.deaths
                val recover = world?.recovered
                val hospitalized = world?.active
                val dangerous = world?.critical

                val df = DecimalFormat(",###")

                totalConfirmed?.text = df.format(confirmed)
                totalDeceased?.text = df.format(deceased)
                recovered?.text = df.format(recover)
                active?.text = df.format(hospitalized)
                critical?.text = df.format(dangerous)
            }

        })
    }


    private fun activeNotification(){

        val alarmUp = PendingIntent.getBroadcast(
            context, 0,Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null

        if (alarmUp) {
            alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            pendingIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            reminder.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_notifications_off_))
            Toast.makeText(requireContext(), "Daily reminder is off!", Toast.LENGTH_SHORT).show()
            Log.d("myTag", "Alarm not activated")
        }else {

            alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            pendingIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 30)
                set(Calendar.SECOND,0)
            }

            Log.d("myTag", "Alarm active now")
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            reminder.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_notifications_active))
            Toast.makeText(requireContext(), "Daily reminder is on!", Toast.LENGTH_SHORT).show()
        }
    }
}