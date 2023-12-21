package com.kkkl.cowieu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.kkkl.cowieu.R
import com.kkkl.cowieu.helper.ParseDataHelper
import kotlinx.android.synthetic.main.dialog_details_layout.iv_close
import kotlinx.android.synthetic.main.dialog_details_layout.tv_button
import kotlinx.android.synthetic.main.dialog_details_layout.tv_c_content

/**
 * ClassName: com.kkkl.cowieu.dialog.DetailsDialog
 * Description: 详情页弹框
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.dialog
 * @date 2023/12/20 09:48
 */
class DetailsDialog(context: Context) : Dialog(context, R.style.Dialog_Style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_details_layout)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        initData()

        iv_close.setOnClickListener {
            dismiss()
        }
        tv_button.setOnClickListener {
            dismiss()
        }
    }

    private fun initData() {
        val configInfo = ParseDataHelper.getConfigJsonData()
        if (configInfo != null) {
            //当rtl为true时，支持文案展示从右往左展示
            if (configInfo.rtl) {
                tv_c_content.textDirection = View.TEXT_DIRECTION_RTL
            } else {
                tv_c_content.textDirection = View.TEXT_DIRECTION_LTR
            }
            if (configInfo.tips?.contents?.isNotEmpty() == true) {
                val s = configInfo.tips?.contents!![0]
                if (!TextUtils.isEmpty(s)) {
                    tv_c_content.text = s
                }
            }

            //按钮文案
            if (configInfo.tips?.confirm?.install?.isNotEmpty() == true) {
                tv_button.text = configInfo.tips?.confirm?.install
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    /**
     * 设置widown的属性
     */
    fun initWindowAttribute() {
        val window = window ?: return
        val lp = window.attributes
        val dm = context.resources.displayMetrics
        lp.width = dm.widthPixels
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER
        lp.dimAmount = 0.5f
        window.attributes = lp
    }
}