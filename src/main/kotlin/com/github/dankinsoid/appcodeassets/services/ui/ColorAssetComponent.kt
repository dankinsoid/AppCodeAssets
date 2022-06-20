package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.ColorSet
import com.github.dankinsoid.appcodeassets.models.Idiom
import com.github.dankinsoid.appcodeassets.models.title
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.vfs.VirtualFile
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.painter.AbstractLayoutPainter
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.*


class ColorAssetComponent(val file: VirtualFile): JPanel(FlowLayout(FlowLayout.LEADING)) {

    private var deviceCheckboxs: MutableMap<Idiom, JCheckBox> = mutableMapOf()

    init {
        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JLabel("Devices"))
            val panel = JPanel().apply {
                alignmentX = LEFT_ALIGNMENT
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                for (device in Idiom.values()) {
                    val checkbox = JCheckBox(device.title)
                    deviceCheckboxs[device] = checkbox
                    add(checkbox)
                }
            }
            add(panel)
        }
        add(panel)
    }

    var colorSet: ColorSet
        get() = ColorSet.get(file)
        set(value) = try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(value)
            file.setBinaryContent(json.toByteArray(Charsets.UTF_8))
        } catch (_: Throwable) {
        }
}
