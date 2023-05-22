package com.fakhrimf.storyapp.utils.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.fakhrimf.storyapp.R
import es.dmoral.toasty.Toasty


class PasswordEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorBtn: Drawable

    private fun init() {
        errorBtn = ContextCompat.getDrawable(context, R.drawable.baseline_error_24) as Drawable
        setOnTouchListener(this)
        doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                if (!isValidPassword(text.toString())) {
                    showError()
                } else {
                    hideError()
                }
            }
        }
//        addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty()) {
//                    if (!isValidEmail(s.toString())) {
//                        showError()
//                    } else {
//                        hideError()
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun showError() {
        setButtonDrawables(endOfTheText = errorBtn)
        error = "Password is less than 8 characters."
    }

    private fun hideError() {
        setButtonDrawables()
    }

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun isValidPassword(target: CharSequence): Boolean {
        return target.length >= 8
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val errorStart: Float
            val errorEnd: Float
            var isClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                errorEnd = (errorBtn.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < errorEnd -> isClicked = true
                }
            } else {
                errorStart = (width - paddingEnd - errorBtn.intrinsicWidth).toFloat()
                when {
                    event.x > errorStart -> isClicked = true
                }
            }
            if (isClicked) {
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        errorBtn = ContextCompat.getDrawable(context, R.drawable.baseline_error_24) as Drawable
                        showError()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        errorBtn = ContextCompat.getDrawable(context, R.drawable.baseline_error_24) as Drawable
                        if (text != null) {
                            if (isValidPassword(text.toString())) {
                                hideError()
                            } else {
                                showError()
                                Toasty.error(context, "Password is less than 8 characters.").show()
                            }
                            return true
                        }
                        return false
                    }
                }
            }
            return false
        }
        return false
    }
}