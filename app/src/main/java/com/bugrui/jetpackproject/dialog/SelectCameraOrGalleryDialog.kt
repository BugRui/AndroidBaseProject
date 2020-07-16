package com.bugrui.jetpackproject.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bugrui.cameralibrary.CameraCompress
import com.bugrui.cameralibrary.openCamera
import com.bugrui.cameralibrary.openGallery
import com.bugrui.jetpackproject.base.BaseDialogFragment
import com.bugrui.jetpackproject.common.GalleryImageEngine
import com.bugrui.jetpackproject.R

/**
 * @Author:            BugRui
 * @CreateDate:        2020/1/3 11:49
 * @Description:       选择相机\相册
 */

fun FragmentActivity.selectCameraOrGallery(requestCode: Int, cancel: () -> Unit? = {}) {
    SelectCameraOrGalleryDialog()
        .setOnSelectCameraClickListener(object : OnSelectCameraClickListener() {

            override fun onCamera() {
                openCamera(
                    requestCode = requestCode,
                    compress = CameraCompress(
                        isCompress = true
                    )
                )
            }

            override fun onGallery() {
                openGallery(
                    requestCode = requestCode,
                    engine = GalleryImageEngine,
                    compress = CameraCompress(
                        isCompress = true
                    )
                )
            }

            override fun onCancel() {
                cancel()
            }

        })
        .showDialog(supportFragmentManager)
}

fun Fragment.selectCameraOrGallery(requestCode: Int, cancel: () -> Unit? = {}) {
    SelectCameraOrGalleryDialog()
        .setOnSelectCameraClickListener(object : OnSelectCameraClickListener() {

            override fun onCamera() {
                openCamera(
                    requestCode = requestCode,
                    compress = CameraCompress(
                        isCompress = true
                    )
                )
            }

            override fun onGallery() {
                openGallery(
                    requestCode = requestCode,
                    engine = GalleryImageEngine,
                    compress = CameraCompress(
                        isCompress = true
                    )
                )
            }

            override fun onCancel() {
                cancel()
            }


        }).showDialog(childFragmentManager)
}

class SelectCameraOrGalleryDialog : BaseDialogFragment() {

    private var listener: OnSelectCameraClickListener? = null

    fun setOnSelectCameraClickListener(listener: OnSelectCameraClickListener): SelectCameraOrGalleryDialog {
        this.listener = listener
        return this
    }

    override fun dialogWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
    override fun dialogHeight(): Int = WindowManager.LayoutParams.WRAP_CONTENT
    override fun dialogGravity(): Int = Gravity.BOTTOM
    override val layoutRes: Int = R.layout.dialog_select_camera_gallery
    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setDialogCancelable(false)
        view.findViewById<TextView>(R.id.tvCamera).setOnClickListener {
            listener?.onCamera()
            dismiss()
        }
        view.findViewById<TextView>(R.id.tvGallery).setOnClickListener {
            listener?.onGallery()
            dismiss()
        }
        view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            listener?.onCancel()
            dismiss()
        }
    }
}

abstract class OnSelectCameraClickListener : ISelectCameraClickListener {

    override fun onCancel() {

    }
}

private interface ISelectCameraClickListener {
    fun onCamera()
    fun onGallery()
    fun onCancel()
}