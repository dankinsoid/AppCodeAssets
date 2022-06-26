package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.Appearances
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.*

fun JComponent.addAppearencesSection(update: () -> Unit): Pair<ComboBox<String>?, JCheckBox?> {
    var appearanceBox: ComboBox<String>? = null
    var highContrastCheckBox: JCheckBox? = null
    addSection("Appearances") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                Box(BoxLayout.PAGE_AXIS).apply {
                    val combobox = ComboBox<String>()
                    combobox.model = DefaultComboBoxModel(Appearances.values().map { it.title }.toTypedArray())
                    combobox.addItemListener { update() }
                    appearanceBox = combobox
                    add(combobox)
                    val box = JCheckBox("High Contrast").apply {
                        alignmentX = Box.LEFT_ALIGNMENT
                        addItemListener { update() }
                    }
                    highContrastCheckBox = box
                    add(box)
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return Pair(appearanceBox, highContrastCheckBox)
}