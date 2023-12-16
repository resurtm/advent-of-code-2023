package com.resurtm.aoc2023.day16

fun launchDay16(testCase: String) {
    println(readInput(testCase))
}

private fun readInput(testCase: String): List<List<Char>> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    val input = mutableListOf<MutableList<Char>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        input.add(rawLine.trim().toMutableList())
    }
    return input
}
