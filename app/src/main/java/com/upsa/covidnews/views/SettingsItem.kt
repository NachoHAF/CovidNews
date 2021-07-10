package com.upsa.covidnews.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.upsa.covidnews.R
import kotlinx.android.synthetic.main.settings_item.view.*

class SettingsItem : LinearLayout {
    @JvmOverloads
    constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.settings_item,this,true
    )
    attrs?.let {
        val typed = context.obtainStyledAttributes(it, R.styleable.SettingsItem,0,0)
        val image = typed.getDrawable(R.styleable.SettingsItem_siIcon)
        val title = typed.getString(R.styleable.SettingsItem_siTitle)
        val value = typed.getString(R.styleable.SettingsItem_siValue)

        icon_settings.setImageDrawable(image)
        title_settings.text = title
        value_settings.text = value
     }
    }
}