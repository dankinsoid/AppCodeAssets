package com.github.dankinsoid.appcodeassets.services.ui

import com.intellij.uiDesigner.core.Spacer
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.geom.Rectangle2D
import javax.swing.*

class Section<Content: JComponent>(val title: JComponent): Box(BoxLayout.PAGE_AXIS) {

    constructor(title: JComponent, content: () -> Content): this(title) {
        setContent(content)
    }
    constructor(title: String, content: () -> Content): this(JLabel("<html><pre>$title</pre></html>"), content)

    init {
        add(title)
        add(createRigidArea(Dimension(0, 5)))
        add(JSeparator(SwingConstants.HORIZONTAL))
        alignmentX = LEFT_ALIGNMENT
    }

    fun setContent(content: () -> Content) {
        removeAll()
        add(title)
        add(createRigidArea(Dimension(0, 5)))
        add(content().apply { alignmentX = LEFT_ALIGNMENT })
        add(JSeparator(SwingConstants.HORIZONTAL))
    }
}

fun <T: JComponent> JComponent.addSection(title: String, block: () -> T) {
    add(
        Section(title) {
            block()
        }
    )
}
