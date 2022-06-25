package com.github.dankinsoid.appcodeassets.actions

import com.github.dankinsoid.appcodeassets.services.Extensions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class NewAppIconsetAction: AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        val data = "\"images\" : []"
        createAsset(Extensions.appiconset.name, "Enter set name", "Create App Icon Set", data, event)
    }

    override fun update(event: AnActionEvent) {
    }
}
