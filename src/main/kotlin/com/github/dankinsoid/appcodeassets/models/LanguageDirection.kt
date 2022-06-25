package com.github.dankinsoid.appcodeassets.models

import com.google.gson.annotations.SerializedName

enum class LanguageDirection {
    @SerializedName("left-to-right") ltr,
    @SerializedName("right-to-left") rtl
}

val LanguageDirection.title: String
    get() = when (this) {
        LanguageDirection.ltr -> "Left to right"
        LanguageDirection.rtl -> "Right to left"
    }