package com.haroldadmin.kshitijchauhan.resuminator.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/* This is an extension function to clear the error on TextInputLayout when the user starts typing in text.
        This is created to be used when the user pressed save when some of the input fields were empty. Now when the user
        starts entering text again, the error should be cleared.*/

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.isEmpty() : Boolean{
    return this.text.isEmpty()
}