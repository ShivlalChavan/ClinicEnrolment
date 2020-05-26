package com.example.clinicenrolment.common

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.clinicenrolment.R

class ToolbarSetup(
    context: AppCompatActivity,
    private val toolbar: Toolbar?,
    title: String,
    backIcon: Int
) {
    private val context: Context

    init {
        this.context = context

        if (toolbar != null) {

            if (!title.matches("".toRegex())) {
                toolbar.title = title
            }
            if (backIcon != 0) {
                toolbar.setNavigationIcon(backIcon)
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_backicon)
            }
            context.setSupportActionBar(toolbar)
        }

    }

    fun hideToolbar() {
        if (toolbar != null) {
            toolbar.visibility = View.GONE
        }
    }
}