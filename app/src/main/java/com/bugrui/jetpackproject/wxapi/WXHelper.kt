package com.bugrui.jetpackproject.wxapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bugrui.buslib.LiveDataBus
import com.bugrui.jetpackproject.wxapi.WXHelper.isCircleOfFriends
import com.bugrui.jetpackproject.ext.toast
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream


/**
 * @Author:            BugRui
 * @CreateDate:        2020/1/13 17:54
 * @Description:       微信sdk
 */
object WXHelper {

    lateinit var iWXAPI: IWXAPI

    /**
     * 判断微信是否支持分享到朋友圈功能
     */
    val isCircleOfFriends: Boolean
        get() = iWXAPI.wxAppSupportAPI >= Build.TIMELINE_SUPPORTED_SDK_INT

    fun regToWx(context: Context, appId: String) {
        iWXAPI = WXAPIFactory.createWXAPI(context, appId, true)
        // 将应用的appId注册到微信
        iWXAPI.registerApp(appId)
        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // 将该app注册到微信
                iWXAPI.registerApp(appId)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }


}


/**
 * 创建事务描述
 */
fun buildTransaction(type: String?): String? {
    return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
}

/**
 * 分享回调观察
 */
private interface ShareObserver {
    fun onReq(p0: BaseReq?)
    fun onResp(p0: BaseResp?)
}

abstract class OnShareObserver : ShareObserver {
    override fun onReq(p0: BaseReq?) {

    }
}


/**
 * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
 *
 * @param bitmap
 * @param maxkb
 * @return
 */
private fun bitmap2Bytes(bitmap: Bitmap, maxkb: Int): ByteArray? {
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    var options = 100
    while (output.toByteArray().size > maxkb && options != 0) {
        output.reset() //清空output
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            options,
            output
        ) //这里压缩options%，把压缩后的数据存放到output中
        options -= 10
    }
    return output.toByteArray()
}


/**
 * 文字类型分享
 * @param text 分享内容文本
 * @param description 分享描述
 */
fun shareToWXText(
    text: String,
    description: String
): SendMessageToWX.Req {
    val textObj = WXTextObject()
    textObj.text = text
    val msg = WXMediaMessage()
    msg.mediaObject = textObj
    msg.description = description
    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("text")
    req.message = msg
    return req
}

/**
 * 图片类型分享
 * @param  thumbBmp 分享图
 */
fun shareToWXImage(thumbBmp: Bitmap): SendMessageToWX.Req {
    val imgObj = WXImageObject(thumbBmp)
    val msg = WXMediaMessage()
    msg.mediaObject = imgObj
    msg.thumbData =
        bitmap2Bytes(thumbBmp, 100)//封面图片，小于128k
    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("img")
    req.message = msg
    return req
}

/**
 * 音乐类型分享
 * @param musicUrl 音乐url
 * @param title 音乐标题
 * @param description 音乐描述
 * @param thumbBmp 封面图片
 */
fun shareToWXMusic(
    musicUrl: String,
    title: String,
    description: String,
    thumbBmp: Bitmap
): SendMessageToWX.Req {
    val music = WXMusicObject()
    music.musicUrl = musicUrl
    val msg = WXMediaMessage()
    msg.mediaObject = music
    msg.title = title
    msg.description = description
    msg.thumbData =
        bitmap2Bytes(thumbBmp, 100)//封面图片，小于128k
    //构造一个Req
    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("music")
    req.message = msg
    return req
}

/**
 * 视频类型分享
 * @param videoUrl 视频url
 * @param title 视频标题
 * @param description 视频描述
 * @param thumbBmp 封面图片
 */
fun shareToWXVideo(
    videoUrl: String,
    title: String,
    description: String,
    thumbBmp: Bitmap
): SendMessageToWX.Req {
    val video = WXVideoObject()
    video.videoUrl = videoUrl
    val msg = WXMediaMessage(video)
    msg.title = title
    msg.description = description
    msg.thumbData =
        bitmap2Bytes(thumbBmp, 100)//封面图片，小于128k
    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("video")
    req.message = msg
    return req
}

/**
 * 网页类型分享
 * @param webUrl  网页url
 * @param title 网页标题
 * @param description 网页描述
 * @param thumbBmp 封面图片
 */
fun shareToWXWeb(
    webUrl: String,
    title: String,
    description: String,
    thumbBmp: Bitmap
): SendMessageToWX.Req {
    val webPage = WXWebpageObject()
    webPage.webpageUrl = webUrl
    val msg = WXMediaMessage(webPage)
    msg.title = title
    msg.description = description
    msg.thumbData =
        bitmap2Bytes(thumbBmp, 100)//封面图片，小于128k
    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("webpage")
    req.message = msg
    return req
}

/**
 * 小程序类型分享,目前只支持分享至会话
 * @param webPageUrl   兼容低版本的网页链接
 * @param userName 小程序的原始 id,小程序原始 ID 获取方法：登录小程序管理后台-设置-基本设置-帐号信息
 * @param path 小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
 * @param miniProgramType 小程序的类型，默认正式版
 *                        正式版: WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
 *                        测试版: WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
 *                        预览版: WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
 * @param title 小程序消息标题
 * @param description 小程序消息描述
 * @param thumbBmp 小程序消息封面图片
 */
fun shareToWXMiniProgram(
    webPageUrl: String,
    userName: String,
    path: String,
    miniProgramType: Int = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE, //默认正式服
    title: String,
    description: String,
    thumbBmp: Bitmap
): SendMessageToWX.Req {
    val miniProgramObj = WXMiniProgramObject()
    miniProgramObj.webpageUrl = webPageUrl
    miniProgramObj.miniprogramType = miniProgramType
    miniProgramObj.userName = userName
    miniProgramObj.path = path
    val msg = WXMediaMessage(miniProgramObj)
    msg.title = title
    msg.description = description
    msg.thumbData = bitmap2Bytes(thumbBmp, 100)
    val req = SendMessageToWX.Req()
    req.transaction =
        buildTransaction("miniProgram")
    req.message = msg
    return req
}

/**
 * @param to
 *          分享到对话: SendMessageToWX.Req.WXSceneSession
 *          分享到朋友圈: SendMessageToWX.Req.WXSceneTimeline
 *          分享到收藏: SendMessageToWX.Req.WXSceneFavorite
 * @param owner 回调观察者必需
 * @param observer 回调观察者,必须传入LifecycleOwner
 */
fun SendMessageToWX.Req.send(
    to: Int = SendMessageToWX.Req.WXSceneSession,
    owner: ComponentActivity? = null,
    observer: OnShareObserver? = null
) {
    if (!WXHelper.iWXAPI.isWXAppInstalled) {
        toast("没有安装微信客户端")
        return
    }
    if (to == SendMessageToWX.Req.WXSceneTimeline && !isCircleOfFriends) {
        toast("微信不支持分享到朋友圈")
        return
    }
    scene = to
    WXHelper.iWXAPI.sendReq(this)  //调用api接口，发送数据到微信
    if (observer == null || owner == null) return
    LiveDataBus.with(TAG_REQ)
        .observe(owner, Observer {
            if (it is BaseReq) {
                observer.onReq(it)
            }
        })
    LiveDataBus.with(TAG_RESP)
        .observe(owner, Observer {
            if (it is BaseResp) {
                observer.onResp(it)
            }
        })
}

/**
 * @param to
 *          分享到对话: SendMessageToWX.Req.WXSceneSession
 *          分享到朋友圈: SendMessageToWX.Req.WXSceneTimeline
 *          分享到收藏: SendMessageToWX.Req.WXSceneFavorite
 * @param owner 回调观察者必需
 * @param observer 回调观察者,必须传入LifecycleOwner
 */
fun SendMessageToWX.Req.send(
    to: Int = SendMessageToWX.Req.WXSceneSession,
    owner: Fragment? = null,
    observer: OnShareObserver? = null
) {
    if (!WXHelper.iWXAPI.isWXAppInstalled) {
        toast("没有安装微信客户端")
        return
    }
    if (to == SendMessageToWX.Req.WXSceneTimeline && !isCircleOfFriends) {
        toast("微信不支持分享到朋友圈")
        return
    }
    scene = to
    WXHelper.iWXAPI.sendReq(this)  //调用api接口，发送数据到微信
    if (observer == null || owner == null) return
    LiveDataBus.with(TAG_REQ)
        .observe(owner, Observer {
            if (it is BaseReq) {
                observer.onReq(it)
            }
        })
    LiveDataBus.with(TAG_RESP)
        .observe(owner, Observer {
            if (it is BaseResp) {
                observer.onResp(it)
            }
        })
}


const val TAG_REQ = "onReq"
const val TAG_RESP = "onResp"