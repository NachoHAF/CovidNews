package com.upsa.covidnews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.upsa.covidnews.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        theme.setOnClickListener{
            CustomDialog().show(childFragmentManager, "theme_dialog")
        }

        back_icon.setOnClickListener{
            findNavController().navigate(R.id.action_settingsFragment_to_helpFragment)
        }

        language.setOnClickListener{
            Toast.makeText(
                requireContext(),
                "Debug version support change languages between English and Spanish!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}