package com.github.dankinsoid.appcodeassets.services

import com.intellij.lang.Language

class ContentJSONLanguage: Language("contents.json") {

    companion object {
        val INSTANCE = ContentJSONLanguage()
    }
}