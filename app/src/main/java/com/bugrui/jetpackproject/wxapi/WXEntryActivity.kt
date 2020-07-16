package com.bugrui.jetpackproject.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bugrui.buslib.LiveDataBus
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : Activity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WXHelper.iWXAPI.handleIntent(intent,this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        WXHelper.iWXAPI.handleIntent(intent,this)
    }

    override fun onReq(p0: BaseReq?) {
        LiveDataBus.send(TAG_REQ,p0)
    }
    override fun onResp(p0: BaseResp?) {
        LiveDataBus.send(TAG_RESP,p0)
        finish()
    }
}