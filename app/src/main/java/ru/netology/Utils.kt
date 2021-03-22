package ru.netology

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {
    companion object {
        fun valueUpgrade(count: Int): String {
            return when (count) {
                in 0..999 -> count.toString()
                in 1000..1099 -> String.format((count / 1000).toString() + "K")
                in 1099..9999 -> String.format("%.1f", (kotlin.math.floor(count / 100.toDouble()))/10) + "K"
                in 10000..99999 -> String.format((count / 1000).toString() + "K")
                in 100000..999999 -> String.format((count / 1000).toString() + "K")
                in 1000000..1099999 -> String.format((count / 1000000).toString() + "M")
                else -> String.format((count / 1000000.toDouble()).toString() + "M")
            }
        }
    }


}

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


