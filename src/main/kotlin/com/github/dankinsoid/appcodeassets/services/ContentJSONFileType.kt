package com.github.dankinsoid.appcodeassets.services

import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class ContentJSONFileType: LanguageFileType(ContentJSONLanguage.INSTANCE) {

    override fun getName(): String {
        return "Contents.json"
    }

    override fun getDescription(): String {
        return "Asset Contents.json file"
    }

    override fun getDefaultExtension(): String {
        return "json"
    }

    override fun getIcon(): Icon {
        return AllIcons.FileTypes.Json
    }

    companion object {

        var INSTANCE: ContentJSONFileType = ContentJSONFileType()
    }
}
