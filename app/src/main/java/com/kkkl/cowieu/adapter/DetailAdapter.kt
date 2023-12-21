package com.kkkl.cowieu.adapter

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kkkl.cowieu.R
import com.kkkl.cowieu.bean.QualaConfigBean
import com.kkkl.cowieu.bean.QualaListBean.DescriptionsBean
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.util.UIUtils

/**
 * ClassName: com.kkkl.cowieu.adapter.DetailAdapter
 * Description: 详情适配器
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.adapter
 * @date 2023/12/20 21:00
 */
class DetailAdapter :
    BaseQuickAdapter<DescriptionsBean?, MyBaseViewHolder>(R.layout.item_detial_content, null) {
    private var qualaConfigBean: QualaConfigBean? = null

    init {
        qualaConfigBean = ParseDataHelper.getConfigJsonData()
    }

    /**
     * html设置显示内容
     */
    private fun setHtmlContent(textView: TextView, content: String?) {
        if (TextUtils.isEmpty(content)) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
            } else {
                textView.text = Html.fromHtml(content)
            }
        }
    }

    override fun convert(holder: MyBaseViewHolder, item: DescriptionsBean?) {
        try {
            if (item == null) {
                return
            }
            setHtmlContent(holder.getView(R.id.tv_detail_title), item.title)
            val builder = StringBuilder()
            if (item.contents?.isNotEmpty() == true) {
                for (content in item.contents!!) {
                    builder.append(content)
                }
            }
            setHtmlContent(holder.getView(R.id.tv_detail_desc), builder.toString())
            //当rtl为true时，支持文案展示从右往左展示
            UIUtils.setTextDirection(
                qualaConfigBean?.rtl == true,
                holder.getView(R.id.tv_detail_title),
                holder.getView(R.id.tv_detail_desc)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}