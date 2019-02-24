package com.mzkii.dev.textcolorreplacer

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.widget.TextView
import com.mzkii.dev.textcolorreplacer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val overLimitColorSpan = BackgroundColorSpan(Color.GRAY)
    private val defaultColorSpan = BackgroundColorSpan(Color.TRANSPARENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(binding) {
            submit.setOnClickListener { editText.text.clear() }
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s == null) return
                    Spannable.Factory.getInstance().newSpannable(editText.text).apply {
                        if (s.length <= INPUT_LIMIT) {
                            removeSpan(overLimitColorSpan)
                            setSpan(defaultColorSpan, 0, s.length, getSpanFlags(defaultColorSpan))
                        } else {
                            removeSpan(defaultColorSpan)
                            setSpan(overLimitColorSpan, INPUT_LIMIT, s.length, getSpanFlags(overLimitColorSpan))
                        }
                    }.also {
                        val selectionStart = editText.selectionStart
                        val selectionEnd = editText.selectionEnd
                        editText.removeTextChangedListener(this)
                        editText.setText(it, TextView.BufferType.SPANNABLE)
                        editText.setSelection(selectionStart, selectionEnd)
                        editText.addTextChangedListener(this)
                    }
                    updateSubmit(editText.text)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
        updateSubmit("")
    }

    private fun updateSubmit(inputText: CharSequence) {
        with(binding) {
            val textDiff = INPUT_LIMIT - inputText.length
            val canSubmit = textDiff >= 0
            textCount.setTextColor(if (canSubmit) Color.BLACK else Color.RED)
            textCount.text = textDiff.toString()
            submit.isEnabled = canSubmit
        }
    }

    companion object {
        private const val INPUT_LIMIT = 10
    }
}
