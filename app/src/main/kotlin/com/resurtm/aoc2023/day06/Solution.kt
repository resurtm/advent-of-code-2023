package com.resurtm.aoc2023.day06

fun launchDay06(testCase: String) {
    val inputData = readInputData(testCase)
    println("Day 06, part 1: ${calcPart1(inputData)}")
}

private fun calcPart1(inp: InputData): Int {
    var result = 1
    for (race in inp) {
        var ways = 0
        for (hold in 0..race.first) {
            val distance = (race.first - hold) * hold
            if (distance > 0 && distance > race.second) ways++
        }
        result *= ways
    }
    return result
}

private fun readInputData(testCase: String): InputData {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    val timeLine = reader.readLine() ?: throw ex
    val distLine = reader.readLine() ?: throw ex
    return prepLine(timeLine) zip prepLine(distLine)
}

private fun prepLine(line: String): List<Int> =
    line.split(":")[1].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

typealias InputData = List<Pair<Int, Int>>
