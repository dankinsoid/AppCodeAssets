package com.github.dankinsoid.appcodeassets.services

import com.github.dankinsoid.appcodeassets.services.ui.ColorAssetComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.ui.PerspectiveFileEditor
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class ContentJSONEditor(project: Project, file: VirtualFile): PerspectiveFileEditor(project, file) {

    private var domElement: DomElement? = null

//    override fun <T : Any?> getUserData(key: Key<T>): T? {
//        return null
//    }

//    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
//        key.set
//    }

//    override fun dispose() {
//    }
//
//    override fun getComponent(): JComponent {
//        return JPanel()
//    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    override fun getName(): String {
        return "Content.json"
    }

    override fun setState(state: FileEditorState) {
    }
//
//    override fun isModified(): Boolean {
//        return false
//    }

//    override fun isValid(): Boolean {
//        return true
//    }

//    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
//    }

//    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
//    }

//    override fun getCurrentLocation(): FileEditorLocation? {
//        return null
//    }

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
        return when (extension) {
            Extensions.colorset -> ColorAssetComponent(file)
            else -> JPanel()
        }
    }
}
