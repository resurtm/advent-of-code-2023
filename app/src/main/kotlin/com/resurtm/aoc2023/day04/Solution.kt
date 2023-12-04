package com.resurtm.aoc2023.day04

import kotlin.math.pow

private fun parseNums(rawNums: String): Set<Int> =
    rawNums.trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }.toSet()

private fun parseLine(line: String): ParseLineResult {
    val parts1 = line.split(":")
    val parts2 = parts1[1].trim().split("|")

    val card = parts1[0].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }[1].trim().toInt()
    val need = parseNums(parts2[0])
    val hands = parseNums(parts2[1])

    val size = need.intersect(hands).size
    val score = 2.0.pow(size - 1).toInt()
    return ParseLineResult(card, score = score, size = size)
}

fun launchDay04(testCase: String) {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    var resultPart1 = 0
    val accum = mutableMapOf<Int, Int>()

    while (true) {
        val res = parseLine(reader.readLine() ?: break)

        val past1 = accum[res.card]
        accum[res.card] = if (past1 == null) 1 else past1 + 1

        repeat(accum[res.card] ?: 1) {
            for (idx in res.card + 1..res.card + res.size) {
                val past2 = accum[idx]
                accum[idx] = if (past2 == null) 1 else past2 + 1
            }
        }

        resultPart1 += res.score
    }

    println("Part 1 answer: $resultPart1")
    println("Part 2 answer: ${accum.values.sum()}")
}

private data class ParseLineResult(val card: Int, val score: Int, val size: Int)
