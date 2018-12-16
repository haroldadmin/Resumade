package com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator

import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.validators.TextValidator
import kotlin.Number

abstract class InputType

abstract class InputProperty(val errorMessage: String)

abstract class InputError

class Text(val text: String): InputType() {
    override fun toString(): String {
        return text
    }
}

class Number(val number: Number): InputType() {
    override fun toString(): String {
        return number.toString()
    }
}

abstract class InputValidator<T: InputType> (protected val input: T) {
    abstract fun validate(): TextValidator
}