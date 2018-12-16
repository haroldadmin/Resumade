package com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator

sealed class TextError: InputError() {
    object Absent: TextError()
    object SizeMismatch: TextError()
    object TooLong: TextError()
    object TooShort: TextError()
    object DoesNotMatchRegExp: TextError()
}

sealed class NumberError: InputError() {
    object Absent: NumberError()
    object TooBig: NumberError()
    object TooLittle: NumberError()
}