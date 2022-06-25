package com.github.dankinsoid.appcodeassets.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

data class AppIconSet(
    var info: Info = Info(),
    var images: List<AppIcon>? = emptyList()
) {

    companion object {
        val cache: MutableMap<String, Pair<AppIconSet, Long>> = mutableMapOf()

        fun get(file: VirtualFile): AppIconSet {
            val cache = AppIconSet.cache[file.path]
            if (cache != null && cache.second == file.timeStamp) {
                return cache.first
            }
            val json = String(file.contentsToByteArray(), Charsets.UTF_8)
            val result = try {
                Gson().fromJson(json, AppIconSet::class.java)
            } catch (_: Throwable) {
                AppIconSet()
            }
            AppIconSet.cache[file.path] = Pair(result, file.timeStamp)
            return result
        }
    }
}

data class AppIcon(
    var filename: String? = null,
    @SerializedName("display-gamut") var displayGamut: Gamut? = null,
    var idiom: Idiom,
    @SerializedName("language-direction") var languageDirection: LanguageDirection? = null,
    var role: Role? = null,
    var scale: String? = null,
    var size: String? = null,
    var subtype: String? = null
) {

    companion object {

        val sets: SortedMap<Idiom, List<AppIconTemplate>> = sortedMapOf(
            Idiom.iphone to listOf(
                AppIconTemplate(sizes = listOf(20.0, 29.0, 40.0, 60.0), scales = listOf(2, 3)),
            ),
            Idiom.iosMarketing to listOf(
                AppIconTemplate(sizes = listOf(1024.0), scales = listOf(1), canUseGamut = false),
            ),
            Idiom.ipad to listOf(
                AppIconTemplate(sizes = listOf(20.0, 29.0, 40.0, 76.0, 83.5), scales = listOf(1, 2)),
            ),
            Idiom.car to listOf(
                AppIconTemplate(sizes = listOf(60.0), scales = listOf(2, 3), canUseGamut = false)
            ),
            Idiom.mac to listOf(
                AppIconTemplate(sizes = listOf(16.0, 32.0, 128.0, 256.0, 512.0), scales = listOf(1, 2), bothDirections = true),
                AppIconTemplate(sizes = listOf(1024.0), scales = listOf(null))
            ),
            Idiom.watch to listOf(
                AppIconTemplate(sizes = listOf(24.0), role = Role.notificationCenter, scales = listOf(2), subtype = "38mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(27.5), role = Role.notificationCenter, scales = listOf(2), subtype = "42mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(33.0), role = Role.notificationCenter, scales = listOf(2), subtype = "45mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(29.0), role = Role.companionSettings, scales = listOf(2, 3), canUseGamut = false),
                AppIconTemplate(sizes = listOf(40.0), role = Role.appLauncher, scales = listOf(2), subtype = "38mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(44.0), role = Role.appLauncher, scales = listOf(2), subtype = "42mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(46.0), role = Role.appLauncher, scales = listOf(2), subtype = "41mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(50.0), role = Role.appLauncher, scales = listOf(2), subtype = "44mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(51.0), role = Role.appLauncher, scales = listOf(2), subtype = "45mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(86.0), role = Role.quickLook, scales = listOf(2), subtype = "38mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(98.0), role = Role.quickLook, scales = listOf(2), subtype = "42mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(108.0), role = Role.quickLook, scales = listOf(2), subtype = "44mm", canUseGamut = false),
                AppIconTemplate(sizes = listOf(117.0), role = Role.quickLook, scales = listOf(2), subtype = "45mm", canUseGamut = false)
            ),
            Idiom.watchMarketing to listOf(
                AppIconTemplate(sizes = listOf(1024.0), scales = listOf(1), canUseGamut = false)
            )
        )
    }
}

data class AppIconTemplate(
    var sizes: List<Double?>,
    var role: Role? = null,
    var scales: List<Int?>,
    var subtype: String? = null,
    var canUseGamut: Boolean = true,
    var bothDirections: Boolean = false
) {

    fun set(idiom: Idiom, file: String?, size: Double?, scale: Int?, gamut: Gamut?, languageDirection: LanguageDirection?): AppIcon {
        return AppIcon(
            filename = file,
            displayGamut = gamut,
            idiom = idiom,
            languageDirection = languageDirection,
            role = role,
            scale = scale?.toScale(),
            size = size?.toSize(),
            subtype = subtype
        )
    }

    fun gamuts(both: Boolean): List<Gamut?> {
        return if (both && canUseGamut) listOf(Gamut.sRGB, Gamut.displayP3) else listOf(null)
    }

    fun directions(): List<LanguageDirection?> {
        return if (bothDirections) listOf(null, LanguageDirection.ltr, LanguageDirection.rtl) else listOf(null)
    }
}

fun Double.format(): String {
    return if (this == this.toLong().toDouble()) String.format("%d", this.toLong()) else String.format("%s", this)
}

fun Double.toSize(): String = this.format().let { "${it}x${it}" }

fun Int.toScale(): String = "${this}x"