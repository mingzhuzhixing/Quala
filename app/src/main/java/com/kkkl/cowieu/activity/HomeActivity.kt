package com.kkkl.cowieu.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kkkl.cowieu.R
import com.kkkl.cowieu.adapter.HomeAdapter
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.event.RefreshCardDataEvent
import com.kkkl.cowieu.event.RefreshListEvent
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.helper.QualaConstants
import com.kkkl.cowieu.helper.ReportEventHelper
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.service.CountDownService
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils
import com.quala.network.decoration.SpaceItemDecoration
import com.quala.network.http.HttpObserver
import com.quala.network.http.HttpRequest
import com.quala.network.http.HttpRetrofit
import com.quala.network.http.HttpSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * ClassName: HomeActivity
 * Description: 首页
 *
 * @author jiaxiaochen
 * @package_name  com.kkkl.cowieu.activity
 * @date 2023/12/19 10:41
 */
class HomeActivity : AppCompatActivity() {

    private var mAdapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        ReportEventHelper.reportShowApp()
        recordFirstOpenAppTime()
        initView()
        initData()
        startCountDownService()
    }

    private fun initView() {
        mAdapter = HomeAdapter(this)

        val dividerItem = SpaceItemDecoration(this, DividerItemDecoration.VERTICAL, false)
        val drawable = ContextCompat.getDrawable(this, R.drawable.shape_divider_line_height_30)
        if (drawable != null) {
            dividerItem.setDrawable(drawable)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(dividerItem)
        }
        recyclerView.adapter = mAdapter
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        //判断列表数据
        val listJson = SPUtils.getListJson()
        if (TextUtils.isEmpty(listJson)) {
            requestListData()
        } else {
            progressBar.visibility = View.GONE
            val listBeans = ParseDataHelper.getListJsonData()
            mAdapter?.setList(listBeans)
            ReportEventHelper.reportJobsShow(listBeans)
        }
    }

    /**
     * 启动定时器service
     */
    private fun startCountDownService() {
        val intent = Intent(this@HomeActivity, CountDownService::class.java)
        startService(intent)
    }

    /**
     * 获取列表数据
     */
    private fun requestListData() {
        val json = JsonObject()
        json.addProperty(QualaConstants.KEY_ATTRIBUTES, SPUtils.getAdjustResult())
        json.addProperty(QualaConstants.KEY_GAID, SPUtils.getGaid())
        HttpRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaJobList(HttpRequest.getRequestBody(json))
            .compose(HttpSchedulers.applySchedulers())
            .subscribe(object : HttpObserver<List<QualaListBean?>>() {
                override fun onSuccess(data: List<QualaListBean?>) {
                    LogUtils.i("onSuccess list: " + data.size)
                    try {
                        progressBar.visibility = View.GONE
                        SPUtils.setListJson(Gson().toJson(data))
                        mAdapter?.setList(data)
                        ReportEventHelper.reportJobsShow(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }


    /**
     * 记录第一次打开app 时间
     */
    private fun recordFirstOpenAppTime() {
        val time = SPUtils.getFirstOpenAppTime()
        LogUtils.d("记录首次启动app 时间:$time")
        if (time > 0) {
            return
        }
        val configEntity = ParseDataHelper.getConfigJsonData()
        val limitHour = configEntity?.contacts?.limits?.hour ?: 0
        LogUtils.d("记录首次启动app 时间 limitHour:$limitHour")
        if (limitHour <= 0) {
            return
        }
        val l = System.currentTimeMillis() + limitHour * 60 * 60 * 1000L
        SPUtils.setFirstOpenAppTime(l)
    }

    /**
     * 刷新 c card数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshData(event: RefreshCardDataEvent?) {
        try {
            if (isDestroyed) {
                return
            }
            val listBeans = ParseDataHelper.getListJsonData()
            val iterator = listBeans.iterator()
            while (iterator.hasNext()) {
                val bean: QualaListBean = iterator.next()
                if ("c" == bean.type) {
                    iterator.remove()
                }
            }
            SPUtils.setListJson(Gson().toJson(listBeans))
            mAdapter?.setList(listBeans)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重新加载工作列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyReloadJobListData(event: RefreshListEvent?) {
        requestListData()
    }

    /**
     * 销毁代码
     */
    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}