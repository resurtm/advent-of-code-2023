package com.resurtm.aoc2023.day12

fun launchDay12(testCase: String) {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    while (true) {
        val rawLine = reader.readLine() ?: break
        parseLine(rawLine)
    }
}

private fun parseLine(rawLine: String) {
    val parts = rawLine.split(' ')
    val mask = parts[0]
    val blocks = parts[1].split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

    println(mask)
    println(blocks)
    println("=====")
}
