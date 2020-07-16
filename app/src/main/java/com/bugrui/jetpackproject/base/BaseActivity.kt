package com.bugrui.jetpackproject.base

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.base.action.OnActivityBackButtonListener
import com.bugrui.jetpackproject.base.action.StatusBarAction
import com.bugrui.jetpackproject.base.action.ToolbarAction
import com.bugrui.jetpackproject.ext.*
import com.bugrui.layout.*
import com.bugrui.library.MultiTypeLayout

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 10:10
 * @Description:       BaseActivity
 */

abstract class BaseActivity : BaseAppCompatActivity(),
    StatusBarAction,
    ToolbarAction {

    private lateinit var rootContainer: LinearLayout
    private lateinit var rootStatusBar: View
    private lateinit var rootToolbar: Toolbar
    private lateinit var toolbarBack: TextView
    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarRightText: TextView
    private lateinit var toolbarRightIcon: ImageView

    var multiTypeLayout: MultiTypeLayout? = null

    override val contentLayoutResId: Int = R.layout.root_view_container

    open val openMultiTypeLayout: Boolean = false

    private var activityBackButtonListener: OnActivityBackButtonListener? = null

    override fun activityOnCreate(savedInstanceState: Bundle?) {
        rootContainer = findViewById(R.id.root_container)
        rootStatusBar = findViewById(R.id.root_status_bar)
        rootToolbar = findViewById(R.id.root_toolbar)
        toolbarBack = findViewById(R.id.tv_toolbar_back)
        toolbarTitle = findViewById(R.id.tv_toolbar_title)
        toolbarRightText = findViewById(R.id.tv_toolbar_right_text)
        toolbarRightIcon = findViewById(R.id.iv_toolbar_right_icon)

        if (contentResId != 0) {
            val contextView = LayoutInflater.from(this).inflate(contentResId, null)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            if (openMultiTypeLayout) {
                multiTypeLayout = MultiTypeLayout(this)
                multiTypeLayout?.setContentLayout(contextView)
                multiTypeLayout?.setLoadingLayout(loadingBaseLayoutView)
                multiTypeLayout?.setEmptyLayout(emptyBaseLayoutView)
                multiTypeLayout?.setErrorLayout(errorBaseLayoutView)
                multiTypeLayout?.setNoNetworkLayout(noNetworkBaseLayoutView)
                rootContainer.addView(multiTypeLayout, layoutParams)
            } else {
                rootContainer.addView(contextView, layoutParams)
            }

        }

        rootStatusBar.visibility = onStatusBarVisibility()
        rootStatusBar.setBackgroundColor(onStatusBarColor())
        rootStatusBar.setParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        rootToolbar.visibility = View.GONE

        toolbarBack.setTextColor(onBackButtonTextColor())
        toolbarBack.textSize = onBackButtonTextSize()
        toolbarBack.text = onBackButtonText()
        toolbarBack.setCompoundDrawablesWithIntrinsicBounds(
            onBackButtonLeftIcon(), 0, 0, 0
        )
        toolbarBack.setOnClickListener {
            if (activityBackButtonListener == null) {
                onBackPressed()
            } else {
                activityBackButtonListener?.onBackClick(toolbarBack)
            }
        }

        toolbarTitle.setTextColor(onTitleTextColor())
        toolbarTitle.textSize = onTitleTextSize()
        toolbarTitle.typeface = onTitleTypeface()

        toolbarRightText.setTextColor(onRightButtonTextColor())
        toolbarRightText.textSize = onRightButtonTextSize()
        toolbarRightText.visibility = View.GONE

        toolbarRightIcon.setImageResource(onRightIconDrawableRes())
        toolbarRightIcon.visibility = View.GONE

        onCustomizeCreate(savedInstanceState)
        afterOnCreate(savedInstanceState)
    }

    abstract fun afterOnCreate(savedInstanceState: Bundle?)

    @get:LayoutRes
    abstract val contentResId: Int

    open fun onCustomizeCreate(savedInstanceState: Bundle?) {

    }

    override fun setBackButtonListener(listener: OnActivityBackButtonListener) {
        this.activityBackButtonListener = listener
    }

    override fun setToolbarTitle(title: String?) {
        toolbarTitle.text = title
        showToolbar()
    }

    override fun setRightTextButton(text: String?) {
        toolbarRightText.text = text
        showRightTextButton()
    }

    override fun setRightIconButton(res: Int) {
        toolbarRightIcon.setImageResource(res)
        showRightIconButton()
    }

    override fun getStatusBarView(): View {
        return rootStatusBar
    }

    override fun showStatusBar() {
        rootStatusBar.visibility = View.VISIBLE
    }

    override fun hideStatusBar() {
        rootStatusBar.visibility = View.GONE
    }

    override fun showToolbar() {
        rootToolbar.visibility = View.VISIBLE
    }

    override fun hideToolbar() {
        rootToolbar.visibility = View.GONE
    }

    override fun showBackButton() {
        toolbarBack.visibility = View.VISIBLE
    }

    override fun hideBackButton() {
        toolbarBack.visibility = View.GONE
    }

    override fun showRightTextButton() {
        toolbarRightText.visibility = View.VISIBLE
    }

    override fun hideRightTextButton() {
        toolbarRightText.visibility = View.GONE
    }

    override fun showRightIconButton() {
        toolbarRightIcon.visibility = View.VISIBLE
    }

    override fun hideRightIconButton() {
        toolbarRightIcon.visibility = View.GONE
    }


}