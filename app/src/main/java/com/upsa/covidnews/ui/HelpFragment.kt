package com.upsa.covidnews.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.upsa.covidnews.R
import com.upsa.covidnews.adapter.ViewpagerItemsAdapter
import com.upsa.covidnews.model.ViewpagerItem
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment: Fragment(),EasyPermissions.PermissionCallbacks {

    companion object{
        const val PERMISSION_CALL_PHONE_REQUEST_CODE = 1
    }

    private lateinit var viewpagerItemsAdapter: ViewpagerItemsAdapter
    private lateinit var indicatorsContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.inflateMenu(R.menu.settings_menu)
        toolbar.setOnMenuItemClickListener{

            when(it.itemId){
              R.id.action_settings ->  findNavController().navigate(R.id.action_helpFragment_to_settingsFragment)
                R.id.action_share -> {
                    val shareBody = "Download CovidNews on Play Store: https://play.google.com/store/apps"
                    val shareSub = "CovidNews provides you with timely information!"

                    val shareIntent = Intent(Intent.ACTION_SEND)

                    shareIntent.type ="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub)
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody)
                    startActivity(shareIntent)
                }
            }
            true
        }


        buttonCall.setOnClickListener{
            requestCallPhonePermission()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.statsFragment)
                }})

        setViewpagerItems()
        setupIndicators()
        setCurrentIndicator(0)
    }


    private fun setViewpagerItems(){
        viewpagerItemsAdapter = ViewpagerItemsAdapter(
            listOf(
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_fever,
                    title = getString(R.string.fever),
                    description = "87.9%"
                ),
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_dry_cough,
                    title = getString(R.string.dry_cough),
                    description = "67.7%"
                ),
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_fatigue,
                    title = getString(R.string.fatigue),
                    description = "38.1%"
                ),
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_sputum,
                    title = getString(R.string.sputum_production),
                    description = "33.4%"
                ),
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_difficulty_breathing,
                    title = getString(R.string.breath),
                    description = "18.6%"
                ),
                ViewpagerItem(
                    viewpagerImage = R.drawable.ic_muscle_pain,
                    title = getString(R.string.pain),
                    description = "14.8%"
                ),
            )
        )
        val viewPagerHelp = ViewPager2
        if (viewPagerHelp != null) {
            viewPagerHelp.adapter = viewpagerItemsAdapter
        }
        viewPagerHelp.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (viewPagerHelp.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicators(){
        indicatorsContainer = view?.findViewById(R.id.indicatorsContainer)!!
        val indicators = arrayOfNulls<ImageView>(viewpagerItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(requireActivity().application)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity().application,
                        R.drawable.background_indicator_inactive
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)

            }
        }
    }

    private fun setCurrentIndicator(position: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity().applicationContext,
                        R.drawable.background_indicator_active
                    )
                )
            }
            else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity().applicationContext,
                        R.drawable.background_indicator_inactive
                    )
                )
            }
        }
    }

    private fun hasCallPhonePermission() =
       EasyPermissions.hasPermissions(
           requireContext(),
           android.Manifest.permission.CALL_PHONE
       )

    private fun requestCallPhonePermission(){
       EasyPermissions.requestPermissions(
           this,
           "Need Call Phone Permission",
           PERMISSION_CALL_PHONE_REQUEST_CODE,
           android.Manifest.permission.CALL_PHONE
       )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestCallPhonePermission()
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
       /* Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()*/
        callPhone()
    }

    private fun callPhone(){
        if (hasCallPhonePermission()){
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "112")
            startActivity(dialIntent)
        }else{
            Toast.makeText(
                requireContext(),
                "error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}