package com.github.dankinsoid.appcodeassets.services.ui

import com.intellij.uiDesigner.core.Spacer
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.geom.Rectangle2D
import javax.swing.*

class Section<Content: JComponent>(title: String, val content: () -> Content): Box(BoxLayout.PAGE_AXIS) {

    init {
        add(JLabel("<html><pre>$title</pre></html>"))
        add(createRigidArea(Dimension(0, 5)))
        add(content().apply { alignmentX = LEFT_ALIGNMENT })
        add(JSeparator(SwingConstants.HORIZONTAL))
        alignmentX = LEFT_ALIGNMENT
    }
}

fun <T: JComponent> JComponent.addSection(title: String, block: () -> T) {
    add(
        Section(title) {
            block()
        }
    )
}
