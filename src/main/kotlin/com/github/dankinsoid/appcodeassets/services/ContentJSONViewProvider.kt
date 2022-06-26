package com.github.dankinsoid.appcodeassets.services

import com.github.dankinsoid.appcodeassets.actions.contentsJSON
import com.github.dankinsoid.appcodeassets.models.ImageSet
import com.github.dankinsoid.appcodeassets.models.Images
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.xml.ui.PerspectiveFileEditor
import com.intellij.util.xml.ui.PerspectiveFileEditorProvider

class ContentJSONViewProvider: PerspectiveFileEditorProvider() {

    private val supported = listOf(Extensions.colorset, Extensions.appiconset, Extensions.imageset)

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (file.parent?.extension == Extensions.imageset.name) {
            val imageSet = ImageSet.get(file)
            return !(imageSet.images?.any { dontSupport(it) } ?: false)
        }
        return file.name == String.contentsJSON && supported.map { it.name }.contains(file.parent?.extension)
    }

    override fun createEditor(project: Project, file: VirtualFile): PerspectiveFileEditor {
        return ContentJSONEditor(project, file)
    }

    private fun dontSupport(image: Images): Boolean {
        return image.widthClass != null || image.heightClass != null || image.memory != null || image.graphicsSet != null
    }
}
