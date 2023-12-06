package com.resurtm.aoc2023.day06

fun launchDay06(testCase: String) {
    println(readInputData(testCase))
}

private fun readInputData(testCase: String): List<Pair<Int, Int>> {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    val timeLine = reader.readLine() ?: throw ex
    val distLine = reader.readLine() ?: throw ex
    return prepLine(timeLine) zip prepLine(distLine)
}

private fun prepLine(line: String): List<Int> =
    line.split(":")[1].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }
