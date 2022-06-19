package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.ColorSet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.vfs.VirtualFile
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel


class ColorAssetComponent(val file: VirtualFile): JComponent() {

    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("Oval Sample")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.layout = GridLayout(2, 2)
        val colors: Array<Color> = arrayOf<Color>(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW)
        for (i in 0..3) {
            val panel = OvalPanel(colors[i])
            panel.add(JButton("asdf"))
            frame.add(panel)
        }
        frame.setSize(300, 200)
        frame.isVisible = true
    }

    fun colorSet(): ColorSet {
        val json = String(file.contentsToByteArray(), Charsets.UTF_8)
        return try { Gson().fromJson(json, ColorSet::class.java) } catch (_: Error) { ColorSet() }
    }

    fun save(colorSet: ColorSet) {
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(colorSet)
            file.setBinaryContent(json.toByteArray(Charsets.UTF_8))
        } catch (_: Error) {}
    }
}