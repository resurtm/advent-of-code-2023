package com.resurtm.aoc2023.day04

import kotlin.math.pow

private fun parseNums(rawNums: String): Set<Int> =
    rawNums.trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }.toSet()

private fun parseLine(line: String): Int {
    val parts1 = line.split(":")
    val parts2 = parts1[1].trim().split("|")

    val cardNum = parts1[0].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }[1].trim().toInt()
    val need = parseNums(parts2[0])
    val hands = parseNums(parts2[1])

    return 2.0.pow(need.intersect(hands).size - 1).toInt()
}

fun launchDay04(testCase: String) {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    var result = 0
    while (true) {
        result += parseLine(reader.readLine() ?: break)
    }
    println("Part 1 answer: $result")
}
