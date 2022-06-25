package com.github.dankinsoid.appcodeassets.extensions

import com.intellij.openapi.vfs.VirtualFile
import java.awt.Dimension
import javax.imageio.ImageIO

fun VirtualFile.pngSize(): Dimension? {
    return try {
        val image = ImageIO.read(this.inputStream)
        Dimension(image.width, image.height)
    } catch (e: Exception) {
        null
    }
}
