package com.github.dankinsoid.appcodeassets.actions

val String.Companion.contentsJSON: String
    get() = "Contents.json"

fun String.Companion.contentsJSONData(data: String?): String {
    var result = """
{
  "info" : {
    "author" : "xcode",
    "version" : 1
  }"""
    if (data != null) {
        result += ",\n  $data\n"
    } else {
        result += "\n"
    }
    result += "}\n"
    return result
}