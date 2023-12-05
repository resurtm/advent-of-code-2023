package com.resurtm.aoc2023.day05

fun launchDay05(testCase: String) {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    while (true) {
        val line = reader.readLine() ?: break
        println(line)
    }
}
