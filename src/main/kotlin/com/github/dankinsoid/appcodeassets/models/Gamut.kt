package com.github.dankinsoid.appcodeassets.models

import com.google.gson.annotations.SerializedName

enum class Gamut {
    sRGB,

    @SerializedName("display-P3")
    displayP3;

    val title: String
        get() = when (this) {
            sRGB -> "sRGB"
            displayP3 -> "Display P3"
        }
}

enum class Gamuts {
    none,
    srgbAndDisplayP3;

    val title: String
        get() = when (this) {
            none -> "None"
            srgbAndDisplayP3 -> "sRGB and DisplayP3"
        }

    val gamuts: List<Gamut?>
        get() = when (this) {
            none -> listOf(null)
            srgbAndDisplayP3 -> listOf(Gamut.sRGB, Gamut.displayP3)
        }

    companion object {
        fun fromArray(array: Array<Gamut>): Gamuts {
            return if (array.isEmpty()) {
                none
            } else {
                srgbAndDisplayP3
            }
        }
    }
}
