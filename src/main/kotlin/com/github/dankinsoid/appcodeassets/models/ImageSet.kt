package com.github.dankinsoid.appcodeassets.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.vfs.VirtualFile

data class ImageSet(
    var info: Info = Info(),
    var images: List<Images>? = listOf(),
    var properties: ImageSetProperties? = null
) {

    companion object {
        val cache: MutableMap<String, Pair<ImageSet, Long>> = mutableMapOf()

        fun get(file: VirtualFile): ImageSet {
            val cache = ImageSet.cache[file.path]
            if (cache != null && cache.second == file.timeStamp) {
                return cache.first
            }
            val json = String(file.contentsToByteArray(), Charsets.UTF_8)
            val result = try {
                Gson().fromJson(json, ImageSet::class.java)
            } catch (_: Throwable) {
                ImageSet()
            }
            ImageSet.cache[file.path] = Pair(result, file.timeStamp)
            return result
        }
    }
}

data class Images(
    var filename: String? = null,
    var idiom: Idiom,
    var appearances: List<Appearance>? = null,
    var scale: String? = null,
    @SerializedName("display-gamut") var gamut: Gamut?,
    @SerializedName("language-direction") var languageDirection: LanguageDirection? = null,

    @SerializedName("width-class") var widthClass: String? = null,
    @SerializedName("height-class") var heightClass: String? = null,
    var memory: String? = null,
    @SerializedName("graphics-feature-set") val graphicsSet: String? = null,
)

data class ImageSetProperties(
    @SerializedName("template-rendering-intent") var templateRenderingIntent: TemplateRenderingIntent? = null,
    @SerializedName("compression-type") var compressionType: CompressionType? = null,
    @SerializedName("preserves-vector-representation") var preservesVectorRepresentation: Boolean? = null
) {

    val isEmpty: Boolean
        get() = templateRenderingIntent == null && compressionType == null && preservesVectorRepresentation != true
}
