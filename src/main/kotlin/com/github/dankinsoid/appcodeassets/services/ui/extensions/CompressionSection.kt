package com.github.dankinsoid.appcodeassets.services.ui.extensions

import com.github.dankinsoid.appcodeassets.models.CompressionType
import com.github.dankinsoid.appcodeassets.services.ui.addSection
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

fun JComponent.addCompressionSection(update: () -> Unit): ComboBox<String>? {
    var comboBox: ComboBox<String>? = null
    addSection("Compression") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                ComboBox<String>().apply {
                    model = DefaultComboBoxModel((listOf(null) + CompressionType.values()).map { it?.title() ?: "Inherited" }.toTypedArray())
                    comboBox = this
                    addItemListener { update() }
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return comboBox
}
