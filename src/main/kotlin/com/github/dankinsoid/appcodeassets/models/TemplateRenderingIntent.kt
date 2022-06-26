package com.github.dankinsoid.appcodeassets.models

enum class TemplateRenderingIntent {
    template,
    original;

    fun title(): String {
        return when (this) {
            template -> "Template"
            original -> "Original"
        }
    }
}