package com.atitto.data.auth

import com.atitto.domain.auth.Sex
import com.atitto.domain.auth.SignUpModel
import com.google.gson.annotations.SerializedName

data class SignUpApi(
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("sex")
    val sex: SexApi,
    @SerializedName("dateOfBirth")
    val dateOfBirth: Long
)

data class EmailCheckApi(
    @SerializedName("email")
    val email: String?
)

enum class SexApi {
    @SerializedName("male")
    MALE,
    @SerializedName("female")
    FEMALE,
    @SerializedName("not_defined")
    NOT_DEFINED
}

fun Sex.toSexApi() =
    when(this) {
        Sex.MALE -> SexApi.MALE
        Sex.FEMALE -> SexApi.FEMALE
        Sex.NOT_DEFINED -> SexApi.NOT_DEFINED
    }

fun SignUpModel.toSignUpApi() =
    SignUpApi(
        email = email,
        firstName = firstName,
        lastName = lastName,
        password = password,
        sex = sex.toSexApi(),
        dateOfBirth = birthDate
    )