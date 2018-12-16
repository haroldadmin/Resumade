package com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.validators

import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.InputValidator
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.Text
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.TextError
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.TextProperty

class TextValidator(input: String) : InputValidator<Text>(Text(input)) {

    private val _errors: MutableSet<Pair<TextError, String>> = HashSet()
    val errors: Set<Pair<TextError, String>>
        get() = _errors

    private val _properties: MutableSet<TextProperty> = HashSet()
    val properties: Set<TextProperty>
        get() = _properties

    fun required(errorMessage: String): TextValidator {
        this._properties.add(TextProperty.Required(errorMessage))
        return this
    }

    fun length(length: Int, errorMessage: String): TextValidator {
        this._properties.add(TextProperty.Length(length, errorMessage))
        return this
    }

    fun minLength(length: Int, errorMessage: String): TextValidator {
        this._properties.add(TextProperty.MinLength(length, errorMessage))
        return this
    }

    fun maxLength(length: Int, errorMessage: String): TextValidator {
        this._properties.add(TextProperty.MaxLength(length, errorMessage))
        return this
    }

    fun matches(exp: Regex, errorMessage: String): TextValidator {
        this._properties.add(TextProperty.RegularExp(exp, errorMessage))
        return this
    }

    fun matches(pattern: String, errorMessage: String): TextValidator {
        val exp = Regex(pattern)
        this._properties.add(TextProperty.RegularExp(exp, errorMessage))
        return this
    }

    override fun validate(): TextValidator {
        val text = input.toString()
        this._properties.forEach { property ->
            when (property) {
                is TextProperty.Required ->
                    if (text.isEmpty()) _errors.add(TextError.Absent to property.errorMessage)
                is TextProperty.Length ->
                    if (text.length != property.length) _errors.add(TextError.SizeMismatch to property.errorMessage)
                is TextProperty.MinLength->
                    if (text.length < property.length) _errors.add(TextError.TooShort to property.errorMessage)
                is TextProperty.MaxLength ->
                    if (text.length > property.length) _errors.add(TextError.TooLong to property.errorMessage)
                is TextProperty.RegularExp ->
                    if (!text.matches(property.exp)) _errors.add(TextError.DoesNotMatchRegExp to property.errorMessage)
            }
        }
        return this
    }
}