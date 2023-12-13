package com.resurtm.aoc2023.day13

fun launchDay13(testCase: String) {
    val input = readInput(testCase)
    input.forEach { pattern ->
        pattern.forEach { println(it) }
        println()
        println()
    }
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    val input: MutableList<List<List<Char>>> = mutableListOf()
    var pattern: MutableList<List<Char>> = mutableListOf()
    var curr = 0

    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) {
            input.add(pattern)
            pattern = mutableListOf()
            curr++
        } else pattern.add(rawLine.toList())
    }

    input.add(pattern)
    return input
}

private typealias Input = List<Pattern>
private typealias Pattern = List<List<Char>>
