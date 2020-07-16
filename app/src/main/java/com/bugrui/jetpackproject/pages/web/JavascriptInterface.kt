package com.bugrui.jetpackproject.pages.web

import android.view.View
import android.webkit.JavascriptInterface
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bugrui.jetpackproject.common.AppConfig
import com.bugrui.jetpackproject.common.AppExecutors
import com.bugrui.jetpackproject.pages.web.WebActivity
import com.bugrui.jetpackproject.utils.LogUtil
import com.tencent.smtt.sdk.WebView


/**
 * @Author: BugRui
 * @CreateDate: 2019/9/12 17:25
 * @Description: java类作用描述
 */
class JavascriptInterface(
    private val activity: WebActivity,
    private val webView: WebView,
    private val toolbar: Toolbar,
    private val titleName: TextView
) {


    /**
     * 关闭web页面
     */
    @android.webkit.JavascriptInterface
    fun close() {
        activity.finish()
    }

    /**
     * 返回应用的版本号
     *
     * @return
     */
    @JavascriptInterface
    fun getVersion(): String? {
        return AppConfig.getVersionName()
    }

    /**
     * 获取平台
     */
    @JavascriptInterface
    fun getPlatform(): String? {
        return "android"
    }



    /**
     * 隐藏标题栏
     */
    @JavascriptInterface
    fun hideActionBar() {
        AppExecutors.get.mainThread().execute {
            toolbar.visibility = View.GONE
        }
    }

    /**
     * 显示标题栏
     */
    @JavascriptInterface
    fun showActionBar() {
        AppExecutors.get.mainThread().execute {
            toolbar.visibility = View.VISIBLE
        }
    }

    /**
     * 设置标题
     */
    @JavascriptInterface
    fun setTitle(title: String?) {
        AppExecutors.get.mainThread().execute {
            titleName.text = title
        }
    }

    /**
     * 清除历史
     */
    @JavascriptInterface
    fun cleanHistory() {
        AppExecutors.get.mainThread().execute {
            webView.clearHistory()
        }
    }

    /**
     * 清除缓存
     */
    @JavascriptInterface
    fun clearCache() {
        AppExecutors.get.mainThread().execute {
            webView.clearCache(true)
        }
    }

    /**
     * 设置文本放大
     */
    @JavascriptInterface
    fun setTextZoom(zoom: Int) {
        AppExecutors.get.mainThread().execute {
            webView.settings.textZoom = zoom
        }
    }

    /**
     * 显示吐司
     */
    @JavascriptInterface
    fun toast(msg: String?) {
        AppExecutors.get.mainThread().execute {
            toast(msg)
        }
    }

    /**
     * 打印日志
     */
    @JavascriptInterface
    fun log(msg: String?) {
        LogUtil.d("bugrui", msg ?: "")
    }

}
