package com.github.dankinsoid.appcodeassets.models

enum class Role {
    notificationCenter,
    companionSettings,
    appLauncher,
    quickLook
}

val Role.title: String
    get() = when (this) {
        Role.notificationCenter -> "Notification Center"
        Role.companionSettings -> "Companion Settings"
        Role.appLauncher -> "App Launcher"
        Role.quickLook -> "Quick Look"
    }
