package com.github.dankinsoid.appcodeassets.services

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.xml.ui.PerspectiveFileEditor
import com.intellij.util.xml.ui.PerspectiveFileEditorProvider

class ContentJSONViewProvider: PerspectiveFileEditorProvider() {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.name == "Contents.json" && Extensions.contains(file.parent?.extension)
    }

    override fun createEditor(project: Project, file: VirtualFile): PerspectiveFileEditor {
        return ContentJSONEditor(project, file)
    }
}
