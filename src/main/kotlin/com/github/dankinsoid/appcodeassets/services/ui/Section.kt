package com.github.dankinsoid.appcodeassets.services.ui

import com.intellij.uiDesigner.core.Spacer
import java.awt.FlowLayout
import java.awt.geom.Rectangle2D
import javax.swing.*

class Section<Content: JComponent>(val title: String, val content: () -> Content): JPanel() {

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JLabel(title))
        add(Box.createRigidArea(java.awt.Dimension(0, 5)))
        val content = this.content()
        add(
            JPanel().apply {
                layout = FlowLayout(FlowLayout.LEFT, 0, 0)
                content.alignmentX = LEFT_ALIGNMENT
                add(content)
                add(Spacer())
                alignmentX = LEFT_ALIGNMENT
            }
        )
        add(JSeparator(SwingConstants.HORIZONTAL))
        alignmentX = LEFT_ALIGNMENT
    }
}