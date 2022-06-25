package com.github.dankinsoid.appcodeassets.actions

import com.github.dankinsoid.appcodeassets.services.Extensions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class NewColorsetAction: AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        val data = """"colors": [
    {
      "idiom": "universal",
      "color": {
        "color-space": "srgb",
        "components": {
          "red": "0x00",
          "green": "0x00",
          "blue": "0x00",
          "alpha": "0xFF"
        }
      }
    }
  ]""".trimIndent()
        createAsset(Extensions.colorset.name, "Enter set name", "Create Color Set", data, event)
    }

    override fun update(event: AnActionEvent) {}
}
