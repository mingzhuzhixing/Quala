package com.kkkl.cowieu.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MyBaseViewHolder(view: View) : BaseViewHolder(view) {

    open fun setGravity(@IdRes viewId: Int, gravity: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        if (view is LinearLayout) {
            view.gravity = gravity
        } else if (view is RelativeLayout) {
            view.gravity = gravity
        }
        return this
    }

    open fun setTextDirection(@IdRes viewId: Int, textDirection: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        if (view is TextView) {
            view.textDirection = textDirection
        }
        return this
    }
}