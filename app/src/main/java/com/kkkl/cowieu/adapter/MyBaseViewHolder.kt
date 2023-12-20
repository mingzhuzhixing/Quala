package com.kkkl.cowieu.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder
/**
 * ClassName: com.kkkl.cowieu.adapter.MyBaseViewHolder
 * Description: 自定义ViewHolder
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.adapter
 * @date 2023/12/20 21:02
 */
class MyBaseViewHolder(view: View) : BaseViewHolder(view) {

    fun setGravity(@IdRes viewId: Int, gravity: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        if (view is LinearLayout) {
            view.gravity = gravity
        } else if (view is RelativeLayout) {
            view.gravity = gravity
        }
        return this
    }

    fun setTextDirection(@IdRes viewId: Int, textDirection: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        if (view is TextView) {
            view.textDirection = textDirection
        }
        return this
    }

    fun setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener) {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
    }
}