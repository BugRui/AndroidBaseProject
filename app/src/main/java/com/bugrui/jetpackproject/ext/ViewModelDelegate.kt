package com.bugrui.jetpackproject.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/10 16:49
 * @Description:       java类作用描述
 */
class ViewModelDelegate<out T : ViewModel>(private val clazz: KClass<T>, private val fromActivity: Boolean) {

    private var viewModel: T? = null

    operator fun getValue(thisRef: FragmentActivity, property: KProperty<*>) = buildViewModel(activity = thisRef)

    operator fun getValue(thisRef: Fragment, property: KProperty<*>) = if (fromActivity)
        buildViewModel(activity = thisRef.activity as? AppCompatActivity
            ?: throw IllegalStateException("Activity must be as BaseActivity"))
    else buildViewModel(fragment = thisRef)

    private fun buildViewModel(activity: FragmentActivity? = null, fragment: Fragment? = null): T {
        if (viewModel != null) return viewModel!!
        activity?.let {
            viewModel =  ViewModelProvider(it).get(clazz.java)
        } ?: fragment?.let {
            viewModel =  ViewModelProvider(it).get(clazz.java)
        } ?: throw IllegalStateException("Activity or Fragment is null! ")
        return viewModel!!
    }
}

fun <T : ViewModel> FragmentActivity.viewModelDelegate(clazz: KClass<T>) =
    ViewModelDelegate(clazz, true)

/**
 * fromActivity默认为true，viewModel生命周期默认跟activity相同
 */
fun <T : ViewModel> Fragment.viewModelDelegate(clazz: KClass<T>, fromActivity: Boolean = true) =
    ViewModelDelegate(clazz, fromActivity)