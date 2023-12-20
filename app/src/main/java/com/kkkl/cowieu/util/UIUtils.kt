package com.kkkl.cowieu.util

import android.view.View
import android.widget.TextView

object UIUtils {

    /***
     * 设置文字方向
     */
    fun setTextDirection(textDirection: Int, vararg textViews: TextView) {
        textViews.forEach {
            it.textDirection = textDirection
        }
    }

    fun setTextDirection(rtl: Boolean, vararg textViews: TextView) {
        if (rtl) {
            setTextDirection(View.TEXT_DIRECTION_RTL, *textViews)
        } else {
            setTextDirection(View.TEXT_DIRECTION_LTR, *textViews)
        }
    }
}