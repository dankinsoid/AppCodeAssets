package com.github.dankinsoid.appcodeassets.actions

import com.github.dankinsoid.appcodeassets.services.hasAncestor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.project.DumbAware

class NewAssets: AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(PlatformCoreDataKeys.VIRTUAL_FILE)
        if (file?.isDirectory != true) {
            event.presentation.isVisible = false
            return
        }
        val inAssets = file.hasAncestor {
            it.extension == "xcassets"
        }
        event.presentation.isVisible = !inAssets
    }
}