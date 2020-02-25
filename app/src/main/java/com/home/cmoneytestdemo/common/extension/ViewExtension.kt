package com.home.cmoneytestdemo.common.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.home.cmoneytestdemo.R

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * avoid continuous clicks
 */
inline fun View.click(crossinline function: (view: View) -> Unit) {
    this.setOnClickListener {
        val tag = this.getTag(R.id.id_tag_click)
        val millisecond = 1000
        if (tag == null || System.currentTimeMillis() - tag.toString().toLong() > millisecond) {
            this.setTag(R.id.id_tag_click, System.currentTimeMillis())
            function.invoke(it)
        }
    }
}