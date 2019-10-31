package com.atitto.domain.auth

import android.content.Context
import androidx.annotation.StringRes
import com.atitto.domain.R

data class SignUpModel(
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val sex: Sex,
    val password: String
)

enum class Sex(val raw: String, @StringRes val title: Int) {
    NOT_DEFINED("not_defined", R.string.sex_not_defined),
    MALE("male", R.string.sex_male),
    FEMALE("female", R.string.sex_female);

    companion object {
        fun getTitles(context: Context) = values().map { context.resources.getString(it.title) }.toTypedArray()
    }
}