package com.bugrui.jetpackproject.pages

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.bugrui.jetpackproject.R

import com.bugrui.jetpackproject.base.BaseActivity
import com.bugrui.jetpackproject.bean.WebArg
import com.bugrui.jetpackproject.dialog.CommonUseDialog
import com.bugrui.jetpackproject.ext.createViewModel
import com.bugrui.jetpackproject.pages.web.WebActivity
import com.bugrui.jetpackproject.vm.MainViewModel

class MainActivity : BaseActivity() {

    override val contentResId: Int =
        R.layout.activity_main

    private val viewModel by lazy { createViewModel(MainViewModel::class) }

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        showLoading()
        Handler().postDelayed({
            findViewById<TextView>(R.id.textView).text = viewModel.str
            hideLoading()
        },3000)
        findViewById<TextView>(R.id.textView).setOnClickListener {
//            selectCameraOrGallery(111)
            startActivity(WebActivity.intentFor(this, WebArg(
                webUrl = "https://www.baidu.com/s?wd=bugly&rsv_spt=1&rsv_iqid=0xa9ff72900024d881&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&rsv_sug3=2&rsv_sug1=2&rsv_sug7=101&rsv_sug2=0&rsv_btype=i&inputT=177&rsv_sug4=2756"
            )))
//            CommonUseDialog.Builder(
//                text = "弹窗"
//            ).showDialog(supportFragmentManager)
        }


    }

}