package com.kkkl.cowieu.helper

/**
 * ClassName: com.kkkl.cowieu.helper.Constants
 * Description: 常量值
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.helper
 * @date 2023/12/20 11:22
 */
object Constants {
    //------------------------sp key------------------------
    const val KEY_ADJUST_RESULT = "adjust_result"

    //------------------------接口 key------------------------
    const val KEY_GAID = "gaid"
    const val KEY_ATTRIBUTES = "attributes"

    //------------------------Extra key------------------------
    const val EXTRA_JSON = "json"
    const val EXTRA_TYPE = "type"

    //------------------------归因上报的 key------------------------
    /**
     * APP页面启动后，开始执行代码的时候，立即上报
     */
    var APP_SHOW_APP = "app_show_app"

    /**
     * 准备执行 invokeApp 调用逻辑前上报（不需要管invokeApp的结果是否成功）
     */
    var ADDTOCARTLT = "addtocartlt"

    /**
     * 准备执行 invokeApp 调用逻辑前上报（不需要管invokeApp的结果是否成功）
     */
    var ADDTOCARTPV = "addtocartpv"

    /**
     * invokeApp 调用返回 success 的时候 且 当前吊起成功的 contact 的类型是 ws
     */
    var ADDTOCART_WS = "addtocart_ws"

    /**
     * 弹出弹窗A界面的时候
     */
    var CONTACT_SHOW_POPUP = "contact_show_popup"

    /**
     * 当卡片展示的时候（展示的判断条件：卡片是否有成功拉到任意a类或c类的内容）
     */
    var JOBS_SHOW_CARD = "jobs_show_card"

    /**
     * 当卡片展示的时候（展示的判断条件：卡片是否有成功拉到c类的内容）
     */
    var JOBS_SHOW_PTJOB = "jobs_show_ptjob"
}