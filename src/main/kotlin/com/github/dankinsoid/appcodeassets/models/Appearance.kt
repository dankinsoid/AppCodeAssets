package com.github.dankinsoid.appcodeassets.models

data class Appearance(
    var appearance: AppearanceType = AppearanceType.luminosity,
    var value: AppearanceValue = AppearanceValue.dark
) {

    companion object {
        fun list(highContrast: Boolean, appearance: AppearanceValue?): List<Appearance> {
            val appearances: MutableList<Appearance> = mutableListOf()
            if (highContrast) {
                appearances.add(
                    Appearance(
                        AppearanceType.contrast,
                        AppearanceValue.high,
                    )
                )
            }
            if (appearance != null) {
                appearances.add(
                    Appearance(
                        AppearanceType.luminosity,
                        appearance,
                    )
                )
            }
            return appearances
        }
    }
}

val Iterable<Appearance>.luminosity: AppearanceValue?
    get() = firstOrNull { it.appearance == AppearanceType.luminosity }?.value

val Iterable<Appearance>.highContrast: Boolean
    get() = firstOrNull { it.value == AppearanceValue.high } != null

enum class AppearanceValue {
    dark,
    high,
    light;

    val title: String
        get() = when (this) {
            dark -> "Dark"
            high -> "High"
            light -> "Light"
        }
}

enum class AppearanceType {
    luminosity,
    contrast
}

enum class Appearances {
    none,
    anyAndDark,
    anyAndDarkAndLight;

    val title: String
        get() = when (this) {
            none -> "None"
            anyAndDark -> "Any, Dark"
            anyAndDarkAndLight -> "Any, Dark and Light"
        }

    val appearances: List<AppearanceValue?>
        get() = when (this) {
            none -> listOf(null)
            anyAndDark -> listOf(null, AppearanceValue.dark)
            anyAndDarkAndLight -> listOf(null, AppearanceValue.dark, AppearanceValue.light)
        }

    companion object {
        fun fromArray(array: Array<AppearanceValue>): Appearances {
            return if (array.contains(AppearanceValue.light)) {
                anyAndDarkAndLight
            } else if (array.contains(AppearanceValue.dark)) {
                anyAndDark
            } else {
                none
            }
        }
    }
}
