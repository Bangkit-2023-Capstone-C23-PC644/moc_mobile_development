package com.dicoding.medicaloversee.view

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.medicaloversee.R

class PasswordEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s != null && s.length < 8) {
                    context.getString(R.string.error_pass)
                } else {
                    null
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hintPassword)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }
}