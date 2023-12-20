package com.resurtm.aoc2023.day20

fun launchDay20(testCase: String) {
    val input = readInput(testCase)
    println(input)
}

internal fun readInput(testCase: String): List<Mod> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read an input, probably it is invalid")

    val mods = mutableListOf<Mod>()
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) continue
        mods.add(parseRawLine(rawLine))
    }
    return mods
}

internal fun parseRawLine(rawLine: String): Mod {
    val parts = rawLine.split("->").map { it.trim() }

    val type =
        if (parts[0] == "broadcaster") 'b'
        else if (parts[0][0] in arrayOf('%', '&')) parts[0][0]
        else throw Exception("Invalid part detected, cannot read it")

    val name = parts[0].trimStart('%', '&')

    val next = parts[1].split(',').map { it.trim() }

    return Mod(type, name, next)
}

internal data class Mod(val type: Char, val name: String, val next: List<String>)
