package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.TemplateRenderingIntent
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

fun JComponent.addRenderAsSection(update: () -> Unit): ComboBox<String>? {
    var comboBox: ComboBox<String>? = null
    addSection("Render As") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                ComboBox<String>().apply {
                    model = DefaultComboBoxModel((listOf(null) + TemplateRenderingIntent.values()).map { it?.title() ?: "Default" }.toTypedArray())
                    comboBox = this
                    addItemListener { update() }
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return comboBox
}