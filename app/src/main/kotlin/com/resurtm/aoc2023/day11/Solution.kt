package com.resurtm.aoc2023.day11

fun launchDay11(testCase: String) {
    val input = readInput(testCase)
    printStars(input.stars)
}

private fun printStars(stars: Stars) {
    stars.forEach { row ->
        row.forEach { print(if (it == -1) '.' else '#') }
        println()
    }
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    var starIndex = 0
    val stars = mutableListOf<List<Int>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        val row = rawLine.map {
            if (it == '#') starIndex++ else -1
        }
        stars.add(row)
    }
    return Input(stars)
}

private data class Input(var stars: Stars = listOf())

private typealias Stars = List<List<Int>>
