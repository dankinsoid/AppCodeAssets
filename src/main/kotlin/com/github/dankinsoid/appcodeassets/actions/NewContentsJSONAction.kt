package com.github.dankinsoid.appcodeassets.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.project.DumbAware

class NewContentsJSONAction: AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(PlatformCoreDataKeys.VIRTUAL_FILE) ?: return
        if (!file.isDirectory) {
            return
        }

        val runned = com.intellij.openapi.application.ApplicationManager.getApplication()?.runWriteAction {
            try {
                val directory = file.createChildDirectory(this, String.contentsJSON)
                val newFile = directory.createChildData(this, String.contentsJSON)
                newFile.setBinaryContent(String.contentsJSONData(null).toByteArray())
            } catch (e: Throwable) {
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(PlatformCoreDataKeys.VIRTUAL_FILE)
        if (file?.isDirectory != true) {
            event.presentation.isVisible = false
            return
        }
        event.presentation.isVisible = file.inAssets
        val contains = file.children.firstOrNull { it.name == String.contentsJSON } != null
        event.presentation.isEnabled = !contains
    }
}
