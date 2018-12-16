package com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator

sealed class TextProperty(errorMessage: String): InputProperty(errorMessage) {
    class Required(errorMessage: String): TextProperty(errorMessage)
    class Length(val length: Int, errorMessage: String): TextProperty(errorMessage)
    class MinLength(val length: Int, errorMessage: String): TextProperty(errorMessage)
    class MaxLength(val length: Int, errorMessage: String): TextProperty(errorMessage)
    class RegularExp(val exp: Regex, errorMessage: String): TextProperty(errorMessage)
}

sealed class NumberProperty(errorMessage: String): InputProperty(errorMessage) {
    class Required(errorMessage: String): NumberProperty(errorMessage)
    class Min(errorMessage: String): NumberProperty(errorMessage)
    class Max(errorMessage: String): NumberProperty(errorMessage)
}