package com.resurtm.aoc2023.day15

fun launchDay15(testCase: String) {
    val input = readInput(testCase)
    val part1 = input.sumOf { calcHash(it) }
    println("Day 15, part 1: $part1")
}

private fun calcHash(str: String): Int {
    var curr = 0
    str.forEach {
        curr += it.code
        curr *= 17
        curr %= 256
    }
    return curr
}

private fun readInput(testCase: String): List<String> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        return rawLine.split(',')
    }
    return emptyList()
}