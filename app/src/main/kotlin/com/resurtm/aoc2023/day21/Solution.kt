package com.resurtm.aoc2023.day21

fun launchDay21(testCase: String) {
    solvePart1(readInput(testCase))
}

private fun solvePart1(grid: List<List<Char>>) {
    println(grid)
}

private fun readInput(testCase: String): List<List<Char>> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read an input, probably it is invalid")

    val grid = mutableListOf<List<Char>>()
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) continue
        grid.add(rawLine.toList())
    }
    return grid
}
