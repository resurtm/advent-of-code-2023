package com.resurtm.aoc2023.day20

internal fun readInput(testCase: String): Map<String, Mod> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read an input, probably it is invalid")

    val mods = mutableMapOf<String, Mod>()
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) continue

        val mod = parseRawLine(rawLine)
        mods[mod.name] = mod
    }
    return mods
}

internal fun parseRawLine(rawLine: String): Mod {
    val parts = rawLine.split("->").map { it.trim() }

    val type =
        if (parts[0] == "broadcaster")
            'b'
        else if (parts[0][0] in arrayOf('%', '&'))
            parts[0][0]
        else
            throw Exception("Invalid module detected, cannot read it")

    val name = parts[0].trimStart('%', '&')

    val next = parts[1].split(',').map { it.trim() }

    return Mod(type, name, next)
}

internal data class Mod(
    val type: Char,
    val name: String,
    val next: List<String>,
    var on: Boolean = false,
    val inps: MutableMap<String, Boolean> = mutableMapOf()
)

internal data class QueueItem(
    val mod: Mod,
    val high: Boolean = false,
    val prevName: String = "",
)

internal data class PulseInfo(
    val low: Long = 0,
    val high: Long = 0,
)
