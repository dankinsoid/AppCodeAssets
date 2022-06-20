package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ColorPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.uiDesigner.core.Spacer
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.painter.AbstractLayoutPainter
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.beans.Customizer
import javax.swing.*
import javax.swing.border.EmptyBorder

class ColorAssetComponent(val file: VirtualFile): Box(BoxLayout.Y_AXIS) {

    private var deviceCheckboxs: MutableMap<Idiom, JCheckBox> = mutableMapOf()

    init {
        addSection("Devices") {
            Box(BoxLayout.Y_AXIS).apply {
                val box = Box(BoxLayout.Y_AXIS).apply {
                    alignmentX = LEFT_ALIGNMENT
                    for (device in Idiom.values()) {
                        val checkbox = JCheckBox(device.title)
                        deviceCheckboxs[device] = checkbox
                        add(checkbox)
                    }
                }
                add(box)
            }
        }
        addSection("Appearances") {
            Box(BoxLayout.Y_AXIS).apply {
                val combobox = ComboBox<Appearances>()
                combobox.model = DefaultComboBoxModel(Appearances.values())
                add(combobox)
                val box = JCheckBox("High Contrast")
                add(box)
            }
        }

        addSection("Gamut") {
            Box(BoxLayout.Y_AXIS).apply {
                alignmentX = LEFT_ALIGNMENT
                val combobox = ComboBox<Gamuts>()
                combobox.model = DefaultComboBoxModel(Gamuts.values())
                add(combobox)
            }
        }
        for (deivce in Idiom.values()) {
            for (appearance in Appearances.values()) {
                for (highContrast in listOf(true, false)) {
                    for (gamut in Gamuts.values()) {
                        add(
                            Section("Color") {
                                Box(BoxLayout.Y_AXIS).apply {
                                    add(
                                        ComboBox<ColorSpace>().apply {
                                            model = DefaultComboBoxModel(ColorSpace.values())
                                        }
                                    )
                                    add(ColorPanel())
                                }
                            }.apply {
                                isVisible = false
                            }
                        )
                    }
                }
            }
        }
        add(Spacer())
        border = EmptyBorder(10, 10, 10, 10)
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

fun <T: JComponent> JComponent.addSection(title: String, block: () -> T) {
    add(
        Section(title) {
            block()
        }
    )
}