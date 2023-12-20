package com.kkkl.cowieu.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.R
import com.kkkl.cowieu.bean.ConfigBean
import com.kkkl.cowieu.bean.ListBean

/**
 * ClassName: HomeAdapter
 * Description:
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.bean
 * @date 2023/12/19 13:37
 */
@SuppressLint("RtlHardcoded")
class HomeAdapter : BaseQuickAdapter<ListBean?, MyBaseViewHolder>(R.layout.item_home, null) {
    var configBean: ConfigBean? = null

    init {
        configBean = ParseDataHelper.getConfigJsonData()
    }

    override fun convert(holder: MyBaseViewHolder, item: ListBean?) {
        if (item == null) {
            return
        }
        if (!TextUtils.isEmpty(item.tag)) {
            holder.setVisible(R.id.ll_tag, true)
            holder.setText(R.id.tv_tag, item.tag)
        } else {
            holder.setGone(R.id.ll_tag, true)
        }
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_sub_title, item.company)
        holder.setText(R.id.tv_desc, item.address)
        holder.setText(R.id.tv_special, item.salary)

        setTextDirection(holder);
    }

    /**
     * 设置文字的方向
     */
    private fun setTextDirection(holder: MyBaseViewHolder) {
        if (configBean != null) {
            if (configBean?.actions != null) {
                holder.setText(R.id.tv_button, configBean?.actions?.apply ?: "")
            }
            //当rtl为true时，支持文案展示从右往左展示
            if (configBean?.rtl == true) {
                holder.setGravity(R.id.ll_tag, Gravity.RIGHT)
                holder.setTextDirection(R.id.tv_title, View.TEXT_DIRECTION_RTL)
                holder.setTextDirection(R.id.tv_sub_title, View.TEXT_DIRECTION_RTL)
                holder.setTextDirection(R.id.tv_desc, View.TEXT_DIRECTION_RTL)
                holder.setGravity(R.id.ll_special, Gravity.RIGHT)
            } else {
                holder.setGravity(R.id.ll_tag, Gravity.LEFT)
                holder.setTextDirection(R.id.tv_title, View.TEXT_DIRECTION_LTR)
                holder.setTextDirection(R.id.tv_sub_title, View.TEXT_DIRECTION_LTR)
                holder.setTextDirection(R.id.tv_desc, View.TEXT_DIRECTION_LTR)
                holder.setGravity(R.id.ll_special, Gravity.LEFT)
            }
        }
    }
}