package com.github.dankinsoid.appcodeassets.actions

import com.github.dankinsoid.appcodeassets.services.Extensions
import com.github.dankinsoid.appcodeassets.services.hasAncestor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile

class NewAssetGroup: DefaultActionGroup(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isPopupGroup = true
        val file = event.getData(PlatformCoreDataKeys.VIRTUAL_FILE)
        if (file?.isDirectory != true) {
            event.presentation.isVisible = false
            return
        }
        val inSet = file.hasAncestor {
            Extensions.contains(it.extension)
        }
        event.presentation.isVisible = file.inAssets && !inSet
    }
}

val VirtualFile.inAssets: Boolean
    get() = hasAncestor {
        it.extension == "xcassets"
    }