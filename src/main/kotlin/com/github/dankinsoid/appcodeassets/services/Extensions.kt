package com.github.dankinsoid.appcodeassets.services

enum class Extensions {

    appiconset,
    arimageset,
    arresourcegroup,
    brandassets,
    cubetextureset,
    dataset,
    gcdashboardimage,
    gcleaderboard,
    gcleaderboardset,
    iconset,
    imageset,
    imagestack,
    imagestacklayer,
    launchimage,
    mipmapset,
    colorset,
    spriteatlas,
    sticker,
    stickerpack,
    stickersequence,
    textureset,
    complicationset;

    companion object {
        fun create(rawValue: String?): Extensions? {
            return values().firstOrNull { it.name == rawValue }
        }
        fun contains(string: String?): Boolean = Extensions.values().map { it.name }.contains(string ?: "")
    }
}
