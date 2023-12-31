package com.resurtm.aoc2023.day08

import com.resurtm.aoc2023.utils.findListLCM

fun launchDay08(testCase: String) {
    val input = readInput(testCase)
    println("Day 08, part 1: ${calculate(input)}")
    println("Day 08, part 2: ${calculateV2(input)}")
}

private fun calculateV2(inp: Input): Long =
    findListLCM(inp.points.keys.filter { it.last() == 'A' }.map { calculate(inp, start = it, end = "Z") })

private fun calculate(inp: Input, start: String = "AAA", end: String = "ZZZ"): Long {
    var currTurn = 0
    var steps = 0L
    var point = start

    do {
        if (currTurn == inp.turns.length) currTurn = 0

        val nextPoint = inp.points[point] ?: continue
        if (inp.turns[currTurn] == 'L') point = nextPoint.first
        else if (inp.turns[currTurn] == 'R') point = nextPoint.second

        currTurn++
        steps++
    } while (!point.endsWith(end))

    return steps
}

private fun readInput(testCase: String): Input {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    val turns = reader.readLine() ?: throw ex
    val points = mutableMapOf<String, Pair<String, String>>()

    while (true) {
        val line = reader.readLine() ?: break
        if (line.indexOfAny(charArrayOf('=', ',')) == -1) continue

        val temp1 = line.split('=')
        val temp2 = temp1[1].split(',')

        val key = temp1[0].trim()
        val value = Pair(temp2[0].trim().trim('('), temp2[1].trim().trim(')'))
        points[key] = value
    }

    return Input(turns = turns, points = points)
}

private data class Input(val turns: String, val points: MutableMap<String, Pair<String, String>>)
