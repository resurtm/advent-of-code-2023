package com.resurtm.aoc2023.day14

fun launchDay14(testCase: String) {
    val input = readInput(testCase)
    println(input)
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    val rows = mutableListOf<MutableList<Char>>()
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        rows.add(rawLine.toMutableList())
    }
    return rows
}

private typealias Input = MutableList<MutableList<Char>>
