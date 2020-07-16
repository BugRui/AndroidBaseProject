package com.bugrui.jetpackproject.common

import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

abstract class CommonActivityLifecycleBoundObserver(
    @NonNull private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun _onCreate() { onCreate() }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun _onStart() { onStart() }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun _onResume() { onResume() }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun _onPause() { onPause() }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun _onStop() { onStop() }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun _onDestroy() {
        onDestroy()
        owner.lifecycle.removeObserver(this)
    }


    open fun onCreate() {
    }

    open fun onStart() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    open fun onStop() {
    }

    open fun onDestroy() {
    }

}

