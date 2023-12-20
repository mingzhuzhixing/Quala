package com.kkkl.cowieu.bean

/**
 * ClassName: EventBean
 * Description:
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.bean
 * @date 2023/11/22 23:37
 */
class EventBean {
    /**
     * 当 refresh=true 的时候，需要调用/list 接口重新拉取岗位列表，并刷新 UI 界面列表
     * 当 refresh=false 的时候，不做任何额外的操作
     */
    var refresh = false
}