package com.github.dankinsoid.appcodeassets.models

import com.google.gson.annotations.SerializedName

enum class Gamut {
    sRGB,

    @SerializedName("display-P3")
    displayP3;
}
