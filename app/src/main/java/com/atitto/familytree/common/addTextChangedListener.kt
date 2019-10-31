package com.atitto.familytree.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.addTextChangedListener(maxLines: Int? = null, listener: TextWatcher.(text: String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            maxLines?.let {
                if (this@addTextChangedListener.lineCount > maxLines &&
                        s.last().toString() == System.lineSeparator())
                    s.replace(s.length - 1, s.length, "")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke(this, s?.toString() ?: "")
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}