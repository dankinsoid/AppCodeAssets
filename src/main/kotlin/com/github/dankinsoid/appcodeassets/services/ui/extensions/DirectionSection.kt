package com.github.dankinsoid.appcodeassets.services.ui.extensions

import com.github.dankinsoid.appcodeassets.models.LanguageDirections
import com.github.dankinsoid.appcodeassets.services.ui.addSection
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent


fun JComponent.addDirectionSection(update: () -> Unit): ComboBox<String>? {
    var comboBox: ComboBox<String>? = null
    addSection("Direction") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                ComboBox<String>().apply {
                    model = DefaultComboBoxModel(LanguageDirections.values().map { it.title }.toTypedArray())
                    comboBox = this
                    addItemListener { update() }
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return comboBox
}
