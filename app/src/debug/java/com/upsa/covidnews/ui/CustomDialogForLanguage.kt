package com.upsa.covidnews.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.DialogFragment
import com.upsa.covidnews.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.settings_item.view.*
import java.util.*


class CustomDialogForLanguage : DialogFragment() {



    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val appSettingPrefsForLanguage : SharedPreferences = requireActivity().getSharedPreferences("AppSettingPrefsLanguage",0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefsForLanguage.edit()

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.language)
                .setItems(
                    R.array.language_array,
                    DialogInterface.OnClickListener { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item

                        //0 = english
                        //1 = spanish
                        when (which) {
                            0 -> {
                                dialog.cancel()
                                val languageCode = "en"
                                val config = resources.configuration
                                val locale = Locale(languageCode)
                                Locale.setDefault(locale)
                                config.setLocale(locale)
                                resources.updateConfiguration(config, resources.displayMetrics)
                                recreate(requireActivity())
                               sharedPrefsEdit.putInt("Language",0)
                                sharedPrefsEdit.apply()
                               }
                            1 -> {
                                dialog.cancel()
                                val languageCode = "es"
                                val config = resources.configuration
                                val locale = Locale(languageCode)
                                Locale.setDefault(locale)
                                config.setLocale(locale)
                                resources.updateConfiguration(config, resources.displayMetrics)
                                recreate(requireActivity())
                                sharedPrefsEdit.putInt("Language",1)
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