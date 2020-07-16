package com.bugrui.jetpackproject.common.listener

import android.text.Editable
import android.text.TextWatcher

/**
 * @Author:            BugRui
 * @CreateDate:        2020/1/9 9:59
 * @Description:       java类作用描述
 */

abstract class OnTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }



}