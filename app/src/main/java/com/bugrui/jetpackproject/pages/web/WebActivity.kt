package com.bugrui.jetpackproject.pages.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bugrui.cameralibrary.imagePath
import com.bugrui.jetpackproject.base.BaseActivity
import com.bugrui.permission.OnPermissionsTaskListener
import com.bugrui.permission.permissionCheck
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.*
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.base.BaseAppCompatActivity
import com.bugrui.jetpackproject.bean.WebArg
import com.bugrui.jetpackproject.dialog.selectCameraOrGallery
import com.bugrui.jetpackproject.ext.extraDelegate
import com.bugrui.jetpackproject.pages.web.pdf.WebFileLoader
import com.bugrui.jetpackproject.utils.FileUtil
import kotlinx.android.synthetic.main.activity_web.*


/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/17 15:01
 * @Description:       Web
 */


class WebActivity : BaseActivity() {
    override val contentResId: Int=R.layout.activity_web
    private var mWebSettings: WebSettings? = null
    private val mWebArg: WebArg? by extraDelegate(PARAMETER_WEB)
    private var webFileLoader: WebFileLoader? = null
    private var valueCallback: ValueCallback<Array<Uri>>? = null


    override fun afterOnCreate(savedInstanceState: Bundle?) {
        toolbarBack.setOnClickListener {
            onBackPressed()
        }

        toolbarClose.setOnClickListener {
            finish()
        }
        progressBar.max = 100

        toolbarTitle.text = mWebArg?.titleName

        mWebArg?.apply {
            webFileLoader = WebFileLoader(fragmentActivity = this@WebActivity)
            if (webFileLoader!!.isFileFormat(webUrl)) {
                webView.visibility = View.GONE
                progressBar.visibility = View.GONE
                container.addView(
                    webFileLoader?.loadFile(webUrl),
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            } else {
                webView.visibility = View.VISIBLE
                initWebView()
            }
        }
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        mWebSettings = webView.settings
        //如果访问的页面中要与Javascript交互，则webView必须设置支持Javascript
        mWebSettings?.apply {

            javaScriptEnabled = if (mWebArg!!.isFileJavaScriptEnabled) {
                mWebArg!!.isFileJavaScriptEnabled
            } else {
                !(mWebArg?.webUrl != null && mWebArg?.webUrl!!.startsWith("file://"))
            }


            javaScriptCanOpenWindowsAutomatically = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            //支持自动加载图片
            loadsImagesAutomatically = true
            //设置编码格式
            defaultTextEncodingName = "utf-8"
            //设置自适应屏幕
            useWideViewPort = true
            loadWithOverviewMode = true
            databaseEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            setAppCacheEnabled(true)
        }


        //JS交互
        webView.addJavascriptInterface(
            JavascriptInterface(this, webView, toolbar, toolbarTitle),
            "native"
        )

        val webChromeClient = MyWebChromeClient()
        webView.webViewClient = mWebViewClient
        webView.webChromeClient = webChromeClient

        //判断是否是html文本
        if (!TextUtils.isEmpty(mWebArg?.htmlText)) {

            val styleHead = "<style>#foo img{width:100%;}</style> " +
                    "<div id='foo' style='width:100%;'>"

            val styleBottom = "</div>"

            //给每个<p>加上默认字体大小
            val htmlStr = mWebArg?.htmlText!!.replace("<p>", "<p style='font-size:32px'>")

            webView.loadDataWithBaseURL(
                null,
                buildString(styleHead, htmlStr, styleBottom),
                "text/html",
                "utf-8",
                null
            )
        } else {
            webView.loadUrl(getWebUrl(mWebArg?.webUrl!!))
        }


    }

    /**
     * 创建字符串方法，当需要组装2个以上的字符串时请使用这个方法
     * @param element
     * @return
     */
    private fun buildString(vararg element: Any?): String? {
        val sb = StringBuffer()
        for (str in element) {
            sb.append(str)
        }
        return sb.toString()
    }

    /**
     * 获取拼接token的web Url
     */
    private fun getWebUrl(url: String): String {
//        if (userData == null) {
//            return url
//        }
//        val token = userData.token
//        return if (!TextUtils.isEmpty(url) && url.contains("?")) {
//            "$url&token=$token"
//        } else {
//            "$url?&token=$token"
//        }
        return url
    }

    /**
     * 重写WebViewClient可以监听网页的跳转和资源加载等等...
     */
    private val mWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
            if (checkUri(Uri.parse(url))) return true
            return super.shouldOverrideUrlLoading(webView, url)
        }

        /**
         * url 协议检查，判断不为http协议的时候做出相应的处理
         *
         * @param uri -
         * @return
         */
        private fun checkUri(uri: Uri?): Boolean {
            if (null == uri) {
                return false
            }
            try {
                val url = uri.toString()

                //过滤我要融资web页面，30w精美模板垃圾广告
                if (url.contains("eqxiu.com/topic/1596.html")) {
                    this@WebActivity.onBackPressed()
                    return true
                }
                if (url.startsWith("scheme:")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                }

                //文件格式
                if (webFileLoader != null && webFileLoader!!.isFileFormat(url)) {
                    val webArg = WebArg(
                        webUrl = url,
                        isFileJavaScriptEnabled = true
                    )
                    startActivity(intentFor(this@WebActivity, webArg))
                    return true
                }

                // 拨打电话
                if (url.startsWith("tel")) {
                    val intent = Intent(Intent.ACTION_DIAL, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    return true
                }
                if (url.startsWith("msg:")) {
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    startActivity(intent)
                    return true
                }
                //跳转第三方支付宝微信
                if (url.startsWith("alipays:") || url.startsWith("weixin:")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        intent.addCategory("android.intent.category.BROWSABLE")
                        intent.component = null
                        startActivity(intent)
                        return true
                    } catch (e: Exception) {
                        e.stackTrace
                    }
                }
                //跳转页面意图
                if (url.startsWith("intent")) {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onReceivedSslError(
            webView: WebView?,
            sslErrorHandler: SslErrorHandler,
            sslError: SslError?
        ) {
            sslErrorHandler.proceed()
        }


    }

    private inner class MyWebChromeClient : WebChromeClient() {

        override fun onShowFileChooser(
            p0: WebView?,
            p1: ValueCallback<Array<Uri>>?,
            p2: FileChooserParams?
        ): Boolean {
            valueCallback = p1
            val createIntent = p2?.createIntent()
            if (createIntent?.action == "android.intent.action.GET_CONTENT" && createIntent.type == "image/*") {
                this@WebActivity.permissionCheck(arrayOf(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.CAMERA"
                ), object : OnPermissionsTaskListener() {
                    override fun onPermissionsTask() {
                        selectCameraOrGallery(requestCodeCameraOrGallery) {
                            p1?.onReceiveValue(null)
                        }
                    }

                    override fun onDenied() {
                        super.onDenied()
                        p1?.onReceiveValue(null)
                    }

                    override fun onNeverAskAgain() {
                        super.onNeverAskAgain()
                        p1?.onReceiveValue(null)
                    }
                })
                return true
            }

            return super.onShowFileChooser(p0, p1, p2)
        }


        override fun onJsAlert(
            webView: WebView?,
            s: String?,
            s1: String?,
            jsResult: JsResult
        ): Boolean {
            jsResult.confirm()
            return true
        }

        override fun onReceivedTitle(webView: WebView?, title: String?) {
            if (toolbarTitle != null && !TextUtils.isEmpty(title)) {
                toolbarTitle.text = title
            }
            if (toolbarClose != null && webView!!.canGoBack()) {
                toolbarClose.visibility = View.VISIBLE
            } else if (toolbarClose != null && !webView!!.canGoBack()) {
                toolbarClose.visibility = View.GONE
            }

        }


        override fun onProgressChanged(webView: WebView?, i: Int) {
            if (progressBar != null) {
                if (i == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = i
                }
            } else {
                super.onProgressChanged(webView, i)
            }
        }

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        //禁止file协议加载JavaScript
        mWebSettings?.javaScriptEnabled = if (mWebArg!!.isFileJavaScriptEnabled) {
            mWebArg!!.isFileJavaScriptEnabled
        } else {
            !(mWebArg?.webUrl != null && mWebArg?.webUrl!!.startsWith("file://"))
        }
        super.onResume()
        webView?.onResume()
    }


    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    @SuppressWarnings("deprecation")
    override fun onStop() {
        mWebSettings?.javaScriptEnabled = false
        super.onStop()

    }



    override fun onDestroy() {
        super.onDestroy()
        webView?.visibility = View.GONE
        webView?.removeAllViews()
        webView?.destroy()
        container?.removeView(webView)
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView?.goBack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestCodeCameraOrGallery) {
                valueCallback?.onReceiveValue(arrayOf(FileUtil.getFilePathTransformUri(data!!.imagePath)))
            }
        } else {
            valueCallback?.onReceiveValue(null)
        }
    }


    companion object {

        private const val requestCodeCameraOrGallery = 112

        private const val PARAMETER_WEB = "WebArg"

        fun intentFor(context: Context, parameter: WebArg): Intent {
            val webIntent = Intent(context, WebActivity::class.java)
            webIntent.putExtra(PARAMETER_WEB, parameter)
            return webIntent
        }
    }


}

