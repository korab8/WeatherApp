package com.musala.weatherapp.util.extensions

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(msg: String, actionText: String = "Retry", length: Int = Snackbar.LENGTH_LONG, action: View.OnClickListener) {
    Snackbar.make(this, msg, length).setBackgroundTint(Color.RED)
        .setTextColor(Color.WHITE).setAction(actionText, action).show()
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.setVisible(visible: () -> Boolean) =
    if (visible()) {
        this.show()
    } else {
        this.hide()
    }
