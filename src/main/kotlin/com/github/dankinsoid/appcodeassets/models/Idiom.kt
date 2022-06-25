package com.github.dankinsoid.appcodeassets.models

import com.google.gson.annotations.SerializedName

enum class Idiom {

    /// The image works on any device and platform.
    universal,

    /// An image shown app launcher on watchOS
    appLauncher,

    /// An image for the Apple Watch Settings app
    companionSettings,

    /// An image for the App Store icon
    @SerializedName("ios-marketing")
    iosMarketing,

    /// The image is for iPhone devices.
    iphone,

    /// The image is for iPad devices.
    ipad,

    /// The image is for Mac computers.
    mac,

    /// An image for the notification center on watchOS.
    notificationCenter,


    /// An image used for a long look on watchOS.
    quickLook,

    /// The image is for Apple TV.
    tv,

    /// The image is for the Apple Watch devices.
    watch,

    car,

    /// An image for the App Store icon.
    @SerializedName("watch-marketing")
    watchMarketing;
}

val Idiom.title: String
    get() = when (this) {
        Idiom.universal -> "Universal"
        Idiom.appLauncher -> "App Launcher"
        Idiom.companionSettings -> "Companion Settings"
        Idiom.iosMarketing -> "AppStore"
        Idiom.iphone -> "iPhone"
        Idiom.ipad -> "iPad"
        Idiom.mac -> "Mac"
        Idiom.notificationCenter -> "Notification Center"
        Idiom.quickLook -> "Quick Look"
        Idiom.tv -> "Apple TV"
        Idiom.watch -> "Watch"
        Idiom.watchMarketing -> "AppStore Watch"
        Idiom.car -> "CarPlay"
    }
