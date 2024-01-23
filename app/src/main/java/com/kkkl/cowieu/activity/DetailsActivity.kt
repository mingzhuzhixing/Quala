package com.kkkl.cowieu.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kkkl.cowieu.R
import com.kkkl.cowieu.adapter.DetailAdapter
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.dialog.DetailsDialog
import com.kkkl.cowieu.dialog.HomeDialog
import com.kkkl.cowieu.helper.QualaConstants
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.util.UIUtils
import com.kkkl.cowieu.widget.RecyclerDecoration
import kotlinx.android.synthetic.main.activity_details.*

/**
 * ClassName: DetailsActivity
 * Description: 详情页
 *
 * @author zhaowei
 * @package_name  com.kkkl.cowieu.activity
 * @date 2023/12/19 10:43
 */
class DetailsActivity : AppCompatActivity() {
    private var mType: String? = null
    private var qualaListBean: QualaListBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val bundle = intent.extras
        mType = if (bundle != null) bundle.getString(QualaConstants.EXTRA_TYPE, "") else ""
        qualaListBean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(QualaConstants.EXTRA_JSON, QualaListBean::class.java)
        } else {
            bundle?.getParcelable(QualaConstants.EXTRA_JSON)
        }
        initView()
        initListener()
    }

    /**
     * 初始化
     */
    private fun initView() {
        tv_title.text = qualaListBean?.title

        val configEntity = ParseDataHelper.getConfigJsonData()
        if (configEntity?.actions != null) {
            tv_bottom_button.text = configEntity.actions?.apply
        }

        //当rtl为true时，支持文案展示从右往左展示
        UIUtils.setTextDirection(
            configEntity?.rtl ?: false,
            tv_title,
            tv_friendly_reminder,
            tv_friendly_desc
        )

        val detailAdapter = DetailAdapter()

        val dividerItem = RecyclerDecoration(this, DividerItemDecoration.VERTICAL, false)
        dividerItem.setDrawable(resources.getDrawable(R.drawable.shape_divider_line_height_30))

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@DetailsActivity, RecyclerView.VERTICAL, false)
            adapter = detailAdapter
            addItemDecoration(dividerItem)
        }
        detailAdapter.setList(qualaListBean?.descriptions)
    }

    /**
     * 点击监听
     */
    private fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }

        tv_bottom_button.setOnClickListener {
            try {
                if ("c" == mType) {
                    //显示弹框a
                    HomeDialog(this@DetailsActivity, qualaListBean).show()
                } else {
                    //显示弹框c
                    DetailsDialog(this@DetailsActivity).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}