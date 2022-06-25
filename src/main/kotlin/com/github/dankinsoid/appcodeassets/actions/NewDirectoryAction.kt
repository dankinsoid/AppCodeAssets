package com.github.dankinsoid.appcodeassets.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class NewDirectoryAction: AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        createAsset(null, "Enter folder name", "Create Asset Folder", null, event)
    }

    override fun update(event: AnActionEvent) {
    }
}
