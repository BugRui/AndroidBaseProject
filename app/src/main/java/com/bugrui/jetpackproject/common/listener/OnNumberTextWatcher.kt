package com.bugrui.jetpackproject.common.listener

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class OnNumberTextWatcher(
    private val editText: EditText,
    private val decimalDigits: Int = -1
) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if (!s.isNullOrEmpty() && s.indexOf(".") == 0) {
            editText.text = null
            return
        }

        if (!s.isNullOrEmpty() &&
            decimalDigits != -1
            && s.contains(".")
            && (s.length - 1 - s.indexOf(".")) > decimalDigits
        ) {
            //限制小数位数
            val str = s.substring(0, s.indexOf(".") + decimalDigits + 1)
            editText.setText(str)
            if (editText.isFocused) {
                editText.setSelection(str.length)
            }
            return
        }
    }


}