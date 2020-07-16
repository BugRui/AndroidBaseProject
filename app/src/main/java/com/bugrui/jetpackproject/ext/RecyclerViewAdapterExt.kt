package com.bugrui.jetpackproject.ext

import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.recyclerview.widget.RecyclerView
import com.bugrui.jetpackproject.R

fun <VH : RecyclerView.ViewHolder> RecyclerView.bindAdapter(
    adapter: RecyclerView.Adapter<VH>,
    layoutManager: RecyclerView.LayoutManager,
    hasFixedSize: Boolean = true,
    hasLayoutAnimation: Boolean = false,
    itemAnimator: RecyclerView.ItemAnimator?=null,
    @AnimRes animResId: Int = R.anim.base_layout_animation_fall_down
) {
    this.layoutManager = layoutManager
    this.setHasFixedSize(hasFixedSize)
    this.adapter = adapter
    this.itemAnimator = itemAnimator

    //列表item动画
    if (hasLayoutAnimation) {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, animResId)
    }

}

