package com.github.dankinsoid.appcodeassets.models
import com.github.dankinsoid.appcodeassets.services.ColorDirectoryNode
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.vfs.VirtualFile
import java.awt.Color
import java.lang.Double.max
import java.lang.Double.min

data class ColorSet(
    var info: Info = Info(),
    var colors: List<Colors>? = emptyList()
) {
    companion object {
        val cache: MutableMap<String, Pair<ColorSet, Long>> = mutableMapOf()

        fun get(file: VirtualFile): ColorSet {
            val cache = ColorSet.cache[file.path]
            if (cache != null && cache.second == file.timeStamp) {
                return cache.first
            }
            val json = String(file.contentsToByteArray(), Charsets.UTF_8)
            val result = try {
                Gson().fromJson(json, ColorSet::class.java)
            } catch (_: Throwable) {
                ColorSet()
            }
            ColorSet.cache[file.path] = Pair(result, file.timeStamp)
            return result
        }
    }
}

data class Colors(
    @SerializedName("display-gamut") var gamut: Gamut?,
    var idiom: Idiom?,
    var subtype: String?,
    var color: ColorData,
    var appearances: List<ColorAppearance>?
)

data class ColorData(
    var platform: String?,
    var reference: String?,
    @SerializedName("color-space") var colorSpace: ColorSpace?,
    var components: Components?
)

fun ColorData.color(): Color {
    return components?.let {
        return Color(
            it.red.toComponent(),
            it.green.toComponent(),
            it.blue.toComponent(),
            it.alpha.toComponent()
        )
    } ?: Color(0, 0, 0, 0)
}

data class Components(
    var red: String,
    var green: String,
    var blue: String,
    var alpha: String
)

fun String.toComponent(): Int {
    return if (this.startsWith("0x")) {
        this.substring(2).toInt(16)
    } else {
        (max(0.0, min(1.0, this.toDouble())) * 255).toInt()
    }
}

enum class ColorSpace {
    srgb,

    @SerializedName("display-p3")
    displayP3,

    @SerializedName("extended-linear-srgb")
    extendedLinearSrgb,

    @SerializedName("extended-srgb")
    extendedSrgb;
}

data class ColorAppearance(
    var appearance: Appearance = Appearance.luminosity,
    var value: AppearanceValue = AppearanceValue.dark
)

enum class AppearanceValue {
    dark,
    high,
    light
}

enum class Appearance {
    luminosity,
    contrast
}

enum class Appearances {
    none,
    anyAndDark,
    anyAndDarkAndLight;

    val title: String
        get() = when (this) {
            none -> "None"
            anyAndDark -> "Any, Dark"
            anyAndDarkAndLight -> "Any, Dark and Light"
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
}
