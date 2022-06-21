package com.github.dankinsoid.appcodeassets.services

import com.github.dankinsoid.appcodeassets.services.ui.ColorAssetComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.ui.PerspectiveFileEditor
import java.awt.Component.TOP_ALIGNMENT
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane

class ContentJSONEditor(project: Project, file: VirtualFile): PerspectiveFileEditor(project, file) {

    private var domElement: DomElement? = null

    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    override fun getName(): String {
        return "Panel"
    }

    override fun setState(state: FileEditorState) {
        println(state)
    }

    override fun commit() {
    }

    override fun reset() {
    }

    override fun getSelectedDomElement(): DomElement? {
        return domElement
    }

    override fun setSelectedDomElement(element: DomElement?) {
        domElement = element
    }

    override fun createCustomComponent(): JComponent {
        val extension = Extensions.create(file.parent?.extension) ?: return JPanel()
        val content = when (extension) {
            Extensions.colorset -> ColorAssetComponent(file)
            else -> JPanel()
        }
        return JBScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
    }
}
