package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.Gamuts
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

fun JComponent.addGamutSection(update: () -> Unit): ComboBox<String>? {
    var gamutComboBox: ComboBox<String>? = null
    addSection("Gamut") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                ComboBox<String>().apply {
                    model = DefaultComboBoxModel(Gamuts.values().map { it.title }.toTypedArray())
                    gamutComboBox = this
                    addItemListener { update() }
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return gamutComboBox
}