package com.resurtm.aoc2023.day08

fun launchDay08(testCase: String) {
    val input = readInput(testCase)
    println("Day 08, part 1: ${solvePart1(input)}")
    println("Day 08, part 2: ${solvePart2(input)}")
}

private fun solvePart1(inp: Input): Long = calculate(inp)

private fun solvePart2(inp: Input): Long {
    val result = inp.points.keys.filter { it.last() == 'A' }.map { calculate(inp, start = it, end = "Z") }
    return findListLCM(result)
}

private fun calculate(inp: Input, start: String = "AAA", end: String = "ZZZ"): Long {
    var currTurn = 0
    var steps = 0L
    var point = start

    do {
        if (currTurn == inp.turns.length) currTurn = 0

        val nextPoint = inp.points[point]
        if (nextPoint != null) {
            if (inp.turns[currTurn] == 'L') point = nextPoint.first
            else if (inp.turns[currTurn] == 'R') point = nextPoint.second
        }

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

private fun findListLCM(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findItemLCM(result, numbers[i])
    }
    return result
}

private fun findItemLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}
