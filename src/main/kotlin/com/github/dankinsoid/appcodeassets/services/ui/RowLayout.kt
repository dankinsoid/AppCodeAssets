package com.github.dankinsoid.appcodeassets.services.ui

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager

class RowLayout: LayoutManager {

    private val components = ArrayList<Component>()

    override fun addLayoutComponent(name: String?, comp: Component?) {
        comp?.let { components.add(it) }
    }

    override fun removeLayoutComponent(comp: Component?) {
        comp?.let { components.remove(it) }
    }

    override fun preferredLayoutSize(parent: Container?): Dimension {
        return Dimension()
    }

    override fun minimumLayoutSize(parent: Container?): Dimension {
        return Dimension()
    }

    override fun layoutContainer(parent: Container?) {

    }
}
