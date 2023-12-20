package com.kkkl.cowieu.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.kkkl.cowieu.R
import com.kkkl.cowieu.bean.ListBean
import com.kkkl.cowieu.helper.Constants
import com.kkkl.cowieu.helper.InvokeAppHelper
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.helper.ReportEventHelper
import kotlinx.android.synthetic.main.dialog_home_layout.*

/**
 * ClassName: com.kkkl.cowieu.ui.HomeDialog
 * Description: 首页弹框
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.dialog
 * @date 2023/12/20 09:51
 */
class HomeDialog(val activity: Activity, val listBean: ListBean?) :
    Dialog(activity, R.style.Dialog_Style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_home_layout)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        initData()
        iv_close.setOnClickListener {
            dismiss()
        }

        tv_button.setOnClickListener {
            InvokeAppHelper.invokeAppStart(activity, listBean)
            dismiss()
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        val configEntity = ParseDataHelper.getConfigJsonData()
        if (configEntity != null) {
            //当rtl为true时，支持文案展示从右往左展示
            if (configEntity.rtl) {
                tv_a_1.textDirection = View.TEXT_DIRECTION_RTL
                tv_a_2.textDirection = View.TEXT_DIRECTION_RTL
                tv_a_3.textDirection = View.TEXT_DIRECTION_RTL
                tv_tip_text.textDirection = View.TEXT_DIRECTION_RTL
            } else {
                tv_a_1.textDirection = View.TEXT_DIRECTION_LTR
                tv_a_2.textDirection = View.TEXT_DIRECTION_LTR
                tv_a_3.textDirection = View.TEXT_DIRECTION_LTR
                tv_tip_text.textDirection = View.TEXT_DIRECTION_LTR
            }
            if (configEntity.contact?.contents?.isNotEmpty() == true) {
                tv_a_1.visibility = View.GONE
                tv_a_2.visibility = View.GONE
                tv_a_3.visibility = View.GONE
                for (i in 0 until configEntity.contact?.contents?.size!!) {
                    val s = configEntity.contact?.contents?.get(i)
                    when (i) {
                        0 -> {
                            tv_a_1.text = s
                            tv_a_1.visibility = View.VISIBLE
                        }

                        1 -> {
                            tv_a_2.text = s
                            tv_a_2.visibility = View.VISIBLE
                        }

                        2 -> {
                            tv_a_3.text = s
                            tv_a_3.visibility = View.VISIBLE
                        }
                    }
                }
            }

            //按钮文案
            if (!TextUtils.isEmpty(configEntity.chatScript?.actions?.contact)) {
                tv_button.text = configEntity.chatScript?.actions?.contact
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun show() {
        super.show()
        ReportEventHelper.eventReport(Constants.CONTACT_SHOW_POPUP)
    }

    /**
     * 设置widown的属性
     */
    fun initWindowAttribute() {
        val window = window ?: return
        val lp: WindowManager.LayoutParams = window.attributes
        val dm: DisplayMetrics = context.resources.displayMetrics
        lp.width = dm.widthPixels
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER
        lp.dimAmount = 0.5f
        window.attributes = lp
    }
}