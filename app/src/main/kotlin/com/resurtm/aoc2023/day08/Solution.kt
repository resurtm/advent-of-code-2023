package com.resurtm.aoc2023.day08

fun launchDay08(testCase: String) {
    val input = readInput(testCase)
    println("Day 08, part 1: ${solvePart1(input)}")
}

private fun solvePart1(input: Input): Int {
    var currTurn = 0
    var steps = 0
    var point = "AAA"

    do {
        if (currTurn == input.turns.length) currTurn = 0
        val nextPoint = input.points[point]
        if (nextPoint != null) {
            if (input.turns[currTurn] == 'L') point = nextPoint.first
            else if (input.turns[currTurn] == 'R') point = nextPoint.second
        }

        currTurn++
        steps++
    } while (point != "ZZZ")

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
