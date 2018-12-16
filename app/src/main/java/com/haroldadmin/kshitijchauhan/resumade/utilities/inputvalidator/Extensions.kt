package com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.validators.TextValidator

fun String.addValidation() = TextValidator(this)

fun TextValidator.useWith(editText: EditText): TextValidator {
    when {
        this.errors.isEmpty() -> editText.error = null
        else -> this.errors.first()
            .let {
                editText.error = it.second
            }
    }
    return this
}

fun TextValidator.useWith(textInputLayout: TextInputLayout): TextValidator {
    when {
        this.errors.isEmpty() -> textInputLayout.apply {
            isErrorEnabled = false
            isHelperTextEnabled = false
        }
        else -> this.errors.first().let {
            textInputLayout.error = it.second
        }

    }
    return this
}

fun <T: InputType> validateAllEditTexts(validations: Iterable<Pair<InputValidator<T>, EditText>>): Boolean {
    var passed = true
    validations.map { (inputValidation, editText) ->
        inputValidation.validate() to editText
    }.map { (validatedInput, editText) ->
        validatedInput.errors.firstOrNull()?.let {
            editText.error = it.second
            passed = false
        }
    }
    return passed
}

fun <T: InputType> Array<Pair<InputValidator<T>, TextInputLayout>>.validateAll(): Boolean {
    var passed = true
    this.map { (inputValidation, til) ->
        inputValidation.validate() to til
    }.map { (validatedInput, til) ->
        validatedInput.errors.firstOrNull()?.let {
            til.error = it.second
            passed = false
        } ?: run {
            til.isErrorEnabled = false
        }
    }
    return passed
}