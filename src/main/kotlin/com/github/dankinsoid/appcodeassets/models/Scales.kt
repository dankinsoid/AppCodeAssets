package com.github.dankinsoid.appcodeassets.models

enum class Scales {
    single,
    individual,
    individualAndSingle;

    val title: String
        get() = when (this) {
            single -> "Single Scale"
            individual -> "Individual Scales"
            individualAndSingle -> "Individual and Single Scales"
        }

    val scales: List<Int?>
        get() = when (this) {
            single -> listOf(null)
            individual -> listOf(1, 2, 3)
            individualAndSingle -> listOf(null, 1, 2, 3)
        }

    companion object {
        val scales: List<Int?> = listOf(null, 1, 2, 3)

        fun fromArray(array: Array<String?>): Scales {
            return if (array.contains(null) && array.any { it != null }) {
                individualAndSingle
            } else if (array.any { it != null }) {
                individual
            } else {
                single
            }
        }
    }
}