package com.github.dankinsoid.appcodeassets.extensions

fun String.nullIfEmpty(): String? = if (this.isEmpty()) null else this
