package com.github.dankinsoid.appcodeassets.models
import com.google.gson.annotations.SerializedName

data class ColorSet(
    val info: Info = Info(),
    val colors: List<Colors>? = emptyList()
)

data class Colors(
    @SerializedName("display-gamut") val gamut: Gamut?,
    val idiom: Idiom?,
    val color: Color
)

data class Color(
    val platform: String?,
    val reference: String?,
    @SerializedName("color-space") val colorSpace: ColorSpace?,
    val components: Components?
)

data class Components(
    val red: Double,
    val green: Double,
    val blue: Double,
    val alpha: Double
)

enum class ColorSpace {
    srgb,

    @SerializedName("display-p3")
    displayP3;
}
