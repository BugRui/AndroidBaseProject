package com.bugrui.jetpackproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.bugrui.jetpackproject.dialog.LoadingDialog

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 13:36
 * @Description:       BaseFragment
 */
abstract class BaseFragment : Fragment() {

    private var loadingDialog: LoadingDialog? = null

    var isLazyLoad = true //标记懒加载是否已经执行

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null && contentResId != 0) {
            return inflater.inflate(contentResId, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCustomizeViewCreated(view, savedInstanceState)
        afterOnViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (isLazyLoad) {
            isLazyLoad = false
            onLazyLoad()
        }
    }


    abstract fun afterOnViewCreated(view: View, savedInstanceState: Bundle?)

    @get:LayoutRes
    protected abstract val contentResId: Int

    open fun onCustomizeViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * 懒加载
     */
    open fun onLazyLoad() {

    }

    fun showLoading(message: String? = "请稍候…") {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireActivity())
        }
        loadingDialog?.showDialog(message)
    }

    fun hideLoading() {
        loadingDialog?.hideDialog()
    }

    override fun onDestroyView() {
        loadingDialog?.hideDialog()
        loadingDialog = null
        super.onDestroyView()
    }

}