package com.upsa.covidnews.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.upsa.covidnews.R


class CustomDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val appSettingPrefs : SharedPreferences = requireActivity().getSharedPreferences("AppSettingPrefs",0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.theme)
                .setItems(
                    R.array.themes_array,
                    DialogInterface.OnClickListener { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item
                        //0 = light
                        //1 = dark
                        when (which) {
                            0 -> {
                                dialog.cancel()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                sharedPrefsEdit.putBoolean("NightMode",false)
                                sharedPrefsEdit.apply()
                               }
                            1 -> {
                                dialog.cancel()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                sharedPrefsEdit.putBoolean("NightMode",true)
                                sharedPrefsEdit.apply()
                                }
                            else -> { println("error")
                            }
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}