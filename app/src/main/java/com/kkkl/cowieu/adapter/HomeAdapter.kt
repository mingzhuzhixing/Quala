package com.kkkl.cowieu.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kkkl.cowieu.R
import com.kkkl.cowieu.activity.DetailsActivity
import com.kkkl.cowieu.bean.QualaConfigBean
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.dialog.HomeDialog
import com.kkkl.cowieu.helper.QualaConstants
import com.kkkl.cowieu.helper.InvokeAppHelper
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.util.UIUtils

/**
 * ClassName: HomeAdapter
 * Description: 首页适配器
 *
 * @author zhaowei
 * @package_name  com.kkkl.cowieu.bean
 * @date 2023/12/20 13:37
 */
@SuppressLint("RtlHardcoded")
class HomeAdapter(val mActivity: Activity) :
    BaseQuickAdapter<QualaListBean?, MyBaseViewHolder>(R.layout.item_home, null) {
    var qualaConfigBean: QualaConfigBean? = null

    init {
        qualaConfigBean = ParseDataHelper.getConfigJsonData()
    }

    override fun convert(holder: MyBaseViewHolder, item: QualaListBean?) {
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

        setTextDirection(holder)

        holder.setOnClickListener(R.id.ll_root_layout) { openDetailPage(item) }

        holder.setOnClickListener(R.id.tv_button) {
            try {
                if ("a" == item.type) {
                    openDetailPage(item)
                } else {
                    // c类卡片
                    if (qualaConfigBean?.contacts?.showContact == true) {
                        HomeDialog(mActivity, item).show()
                    } else {
                        InvokeAppHelper.invokeAppOpen(mActivity, item)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置文字的方向
     */
    private fun setTextDirection(holder: MyBaseViewHolder) {
        if (qualaConfigBean != null) {
            if (qualaConfigBean?.actions != null) {
                holder.setText(R.id.tv_button, qualaConfigBean?.actions?.apply ?: "")
            }
            //当rtl为true时，支持文案展示从右往左展示
            if (qualaConfigBean?.rtl == true) {
                holder.setGravity(R.id.ll_tag, Gravity.RIGHT)
                holder.setGravity(R.id.ll_special, Gravity.RIGHT)
                UIUtils.setTextDirection(
                    View.TEXT_DIRECTION_RTL,
                    holder.getView(R.id.tv_title),
                    holder.getView(R.id.tv_sub_title),
                    holder.getView(R.id.tv_desc)
                )
            } else {
                holder.setGravity(R.id.ll_tag, Gravity.LEFT)
                holder.setGravity(R.id.ll_special, Gravity.LEFT)
                UIUtils.setTextDirection(
                    View.TEXT_DIRECTION_LTR,
                    holder.getView(R.id.tv_title),
                    holder.getView(R.id.tv_sub_title),
                    holder.getView(R.id.tv_desc)
                )
            }
        }
    }

    /**
     * 打开详情页
     */
    private fun openDetailPage(bean: QualaListBean?) {
        try {
            if (bean == null) {
                return
            }
            val intent = Intent(context, DetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(QualaConstants.EXTRA_JSON, bean)
            bundle.putString(QualaConstants.EXTRA_TYPE, bean.type)
            intent.putExtras(bundle)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}