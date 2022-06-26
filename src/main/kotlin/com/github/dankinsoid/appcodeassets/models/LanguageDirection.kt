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

enum class LanguageDirections {
    fixed,
    ltr,
    rtl,
    both;

    val title: String
        get() = when (this) {
            fixed -> "Fixed"
            ltr -> "Left to Right, Mirrors"
            rtl -> "Right to Left, Mirrors"
            both -> "Both"
        }

    val directions: List<LanguageDirection?>
        get() = when (this) {
            fixed -> listOf(null)
            ltr -> listOf(LanguageDirection.ltr)
            rtl -> listOf(LanguageDirection.rtl)
            both -> listOf(LanguageDirection.ltr, LanguageDirection.rtl)
        }

    companion object {
        val directions: List<LanguageDirection?>
            get() = listOf(null, LanguageDirection.ltr, LanguageDirection.rtl)

        fun fromArray(array: Array<LanguageDirection>): LanguageDirections {
            return if (array.contains(LanguageDirection.ltr) && array.contains(LanguageDirection.rtl)) {
                both
            } else if (array.contains(LanguageDirection.ltr)) {
                ltr
            } else {
                rtl
            }
        }
    }
}
