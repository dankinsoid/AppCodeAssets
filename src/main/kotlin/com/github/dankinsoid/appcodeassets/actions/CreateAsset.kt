package com.github.dankinsoid.appcodeassets.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

fun AnAction.createAsset(extension: String?, message: String, title: String, data: String?, event: AnActionEvent) {
    val file = event.getData(com.intellij.openapi.actionSystem.PlatformCoreDataKeys.VIRTUAL_FILE) ?: return
    val name = Messages.showInputDialog(
        event.project,
        message,
        title,
        null,
        null,
        null
    ) ?: return
    if (name.isEmpty()) {
        return
    }
    val runned = com.intellij.openapi.application.ApplicationManager.getApplication()?.runWriteAction {
        try {
            val directory = file.createChildDirectory(this, "${name}${extension?.let { ".$it" } ?: ""}")
            val newFile = directory.createChildData(this, String.contentsJSON)
            newFile.setBinaryContent(String.contentsJSONData(data).toByteArray())
        } catch (e: Throwable) {
        }
    }
}
