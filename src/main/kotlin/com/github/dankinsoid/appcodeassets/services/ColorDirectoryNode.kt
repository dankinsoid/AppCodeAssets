package com.github.dankinsoid.appcodeassets.services

import com.github.dankinsoid.appcodeassets.models.ColorSet
import com.github.dankinsoid.appcodeassets.models.color
import com.google.common.primitives.Doubles.max
import com.google.gson.Gson
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiDirectory
import java.awt.*
import java.awt.image.ImageObserver
import javax.swing.Icon
import javax.swing.ImageIcon

class ColorDirectoryNode(project: Project, directory: PsiDirectory, settings: ViewSettings?): PsiDirectoryNode(project, directory, settings) {

    private val gson = Gson()

    override fun setupIcon(data: PresentationData?, psiDirectory: PsiDirectory?) {
        if (virtualFile?.extension == null) {
            data?.setIcon(IconLoader.getIcon("/icons/assetsfolder.svg", this.javaClass))
            return
        }
        if (!Extensions.contains(virtualFile?.extension)) {
            super.setupIcon(data, psiDirectory)
            return
        }

        if (virtualFile?.extension == Extensions.imageset.name) {
            data?.setIcon(IconLoader.getIcon("/icons/imageset.svg", this.javaClass))
            return
        }

        if (virtualFile?.extension == Extensions.appiconset.name) {
            data?.setIcon(IconLoader.getIcon("/icons/appiconset.svg", this.javaClass))
            return
        }

        if (virtualFile?.extension != Extensions.colorset.name) {
            super.setupIcon(data, psiDirectory)
            return
        }

        super.setupIcon(data, psiDirectory)
        val set = colorSet()
        val color = set.colors?.firstOrNull()?.color?.color() ?: Color(0, 0, 0, 0)
        data?.setIcon(ColorIcon(16, color, 6))
    }

    override fun updateImpl(data: PresentationData) {
        super.updateImpl(data)
        if (Extensions.contains(virtualFile?.extension) && data.presentableText?.contains(".") == true) {
            data.presentableText = data.presentableText?.substringBefore(".")
        }
    }

    private fun image(): Image? {
        val imageFile = (virtualFile?.children?.firstOrNull { it.extension == "pdf" }) ?: return null

        val image = Toolkit.getDefaultToolkit().createImage(imageFile.contentsToByteArray())

        val width = image.getWidth(object: ImageObserver {
            override fun imageUpdate(img: Image?, x: Int, y: Int, width: Int, height: Int, flags: Int) = true
        }).toDouble()

        val height = image.getHeight(object: ImageObserver {
            override fun imageUpdate(img: Image?, x: Int, y: Int, width: Int, height: Int, flags: Int) = true
        }).toDouble()

        val scale = max(width, height) / 16.0

        return Toolkit.getDefaultToolkit()
            .createImage(imageFile.contentsToByteArray())
            .getScaledInstance((width / scale).toInt(), (height / scale).toInt(), Image.SCALE_SMOOTH)
    }

    private fun colorSet(): ColorSet {
        val file = virtualFile?.children?.firstOrNull { it.name == "Contents.json" } ?: return ColorSet()
        return ColorSet.get(file)
    }

    private class ColorIcon(val size: Int, val color: Color, val radius: Int): Icon {
        override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
            (g as? Graphics2D)?.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            )
            g?.color = color
            g?.fillRoundRect(x, y, size, size, radius, radius)
        }
        override fun getIconWidth() = size
        override fun getIconHeight() = size
    }
}
