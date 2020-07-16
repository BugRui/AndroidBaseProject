package com.bugrui.jetpackproject.bean

import java.io.Serializable

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/18 14:46
 * @Description:       java类作用描述
 */
data class WebArg(
    val webUrl: String,                          //webUrl
    val htmlText: String? = null,                //html文本
    var titleName: String? = null,               //标题文字
    var hideTitle: Boolean = false,              //是否隐藏标题
    var isFileJavaScriptEnabled: Boolean = false //加载本地file时是否开启js
) : Serializable