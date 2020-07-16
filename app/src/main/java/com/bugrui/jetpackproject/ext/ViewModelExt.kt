package com.bugrui.jetpackproject.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

fun <VM : ViewModel> AppCompatActivity.createViewModel(clazz: KClass<VM>): VM {
    return ViewModelProvider(this).get(clazz.java)
}

fun <VM : ViewModel> Fragment.createViewModel(clazz: KClass<VM>): VM {
    return ViewModelProvider(this).get(clazz.java)
}