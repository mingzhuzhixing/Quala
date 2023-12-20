package com.kkkl.cowieu.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.kkkl.cowieu.bean.ListBean
import com.kkkl.cowieu.event.RefreshCardDataEvent
import com.kkkl.cowieu.event.RefreshListEvent
import com.kkkl.cowieu.helper.ParseDataHelper
import com.kkkl.cowieu.helper.ReportEventHelper
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.util.SPUtils
import com.quala.network.decoration.SpaceItemDecoration
import com.quala.network.http.HttpObserver
import com.quala.network.http.HttpRetrofit
import com.quala.network.http.HttpSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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
        initView()
        initData()
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
        val listJson = SPUtils.getJobListJson()
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
     * 获取列表数据
     */
    private fun requestListData() {
        val json = JsonObject()
        json.addProperty("attributes", SPUtils.getAdjustResult())
        json.addProperty("gaid", SPUtils.getGaid())
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        HttpRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaJobList(body)
            .compose(HttpSchedulers.applySchedulers())
            .subscribe(object : HttpObserver<List<ListBean?>>() {
                override fun onSuccess(data: List<ListBean?>) {
                    Log.i("jxc", "onSuccess list: " + data.size)
                    progressBar.visibility = View.GONE
                    SPUtils.setJobListJson(Gson().toJson(data))
                    mAdapter?.setList(data)
                    //todo
                    //AdjustEventReport.reportJobsShow(data)
                }
            })
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
            val listEntities: MutableList<ListBean> = ParseDataHelper.getListJsonData()
            val iterator: MutableIterator<ListBean> = listEntities.iterator()
            while (iterator.hasNext()) {
                val bean: ListBean = iterator.next()
                if ("c".equals(bean.type, ignoreCase = true)) {
                    iterator.remove()
                }
            }
            SPUtils.setJobListJson(Gson().toJson(listEntities))
            mAdapter?.setList(listEntities)
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