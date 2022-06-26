package com.github.dankinsoid.appcodeassets.services.ui.extensions

import com.github.dankinsoid.appcodeassets.models.Appearances
import com.github.dankinsoid.appcodeassets.services.ui.addSection
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.*


fun JComponent.addResizingSection(update: () -> Unit): JCheckBox? {
    var checkBox: JCheckBox? = null
    addSection("Resizing") {
        Box(BoxLayout.LINE_AXIS).apply {
            add(
                Box(BoxLayout.PAGE_AXIS).apply {
                    val box = JCheckBox("Preserve Vector Data").apply {
                        alignmentX = Box.LEFT_ALIGNMENT
                        addItemListener { update() }
                    }
                    checkBox = box
                    add(box)
                }
            )
            add(Box.Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
        }
    }
    return checkBox
}