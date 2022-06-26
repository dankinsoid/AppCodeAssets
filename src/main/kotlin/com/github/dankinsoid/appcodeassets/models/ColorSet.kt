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
    var appearances: List<Appearance>?
)

data class ColorData(
    var platform: String?,
    var reference: String?,
    @SerializedName("color-space") var colorSpace: ColorSpace?,
    var components: Components?
) {

    override fun equals(other: Any?): Boolean {
        val otherColor = (other as? ColorData)?.color() ?: return false
        val color = this.color()
        return color.red == otherColor.red &&
                color.green == otherColor.green &&
                color.blue == otherColor.blue &&
                color.alpha == otherColor.alpha
    }
}

fun ColorData.color(): Color {
    return components?.color() ?: Color(0, 0, 0, 1)
}

data class Components(
    var red: String,
    var green: String,
    var blue: String,
    var alpha: String
) {
    fun color(): Color {
        return Color(
            red.toComponent(),
            green.toComponent(),
            blue.toComponent(),
            alpha.toComponent()
        )
    }
}

fun String.toComponent(): Int {
    return if (this.startsWith("0x")) {
        this.substring(2).toInt(16)
    } else {
        (max(0.0, min(1.0, this.toDouble())) * 255).toInt()
    }
}

fun Color.components(): Components {
    return Components(
        String.format("0x%02X", red),
        String.format("0x%02X", green),
        String.format("0x%02X", blue),
        String.format("0x%02X", alpha)
    )
}

enum class ColorSpace {
    srgb,

    @SerializedName("display-p3")
    displayP3,

    @SerializedName("extended-linear-srgb")
    extendedLinearSrgb,

    @SerializedName("extended-srgb")
    extendedSrgb;

    val title: String
        get() = when (this) {
            srgb -> "sRGB"
            displayP3 -> "Display P3"
            extendedLinearSrgb -> "Extended Linear sRGB"
            extendedSrgb -> "Extended sRGB"
        }

    val gamut: Gamut
        get() = when (this) {
            displayP3 -> Gamut.displayP3
            else -> Gamut.sRGB
        }
}
