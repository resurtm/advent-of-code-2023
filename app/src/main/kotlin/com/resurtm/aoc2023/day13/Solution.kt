package com.resurtm.aoc2023.day13

fun launchDay13(testCase: String) {
    val input = readInput(testCase)
    println("Day 13, part 1: ${calcPart1(input)}")
    println("Day 13, part 2: ${calcPart2(input)}")
}

private fun calcPart2(input: Input): Int {
    var result = 0
    input.forEach { pattern ->
        val res = smudge(pattern)
        result += if (res.first.first) (res.first.second + 1) * 100 else 0
        result += if (res.second.first) res.second.second + 1 else 0
    }
    return result
}

private fun smudge(pattern: Pattern): Pair<Pair<Boolean, Int>, Pair<Boolean, Int>> {
    val h = checkPatternHorizontal(pattern)
    val v = checkPatternVertical(pattern)

    for (row in pattern.indices) {
        for (col in pattern[row].indices) {
            val smudged = pattern.map { it.toMutableList() }
            smudged[row][col] = if (smudged[row][col] == '.') '#' else '.'

            val hor = checkPatternHorizontal(smudged)
            hor.forEach {
                if (h.isNotEmpty() && h.first().first && h.first().second != it.second) return Pair(it, Pair(false, -1))
                else if (v.isNotEmpty() && v.first().first) return Pair(it, Pair(false, -1))
            }

            val ver = checkPatternVertical(smudged)
            ver.forEach {
                if (v.isNotEmpty() && v.first().first && v.first().second != it.second) return Pair(Pair(false, -1), it)
                else if (h.isNotEmpty() && h.first().first) return Pair(Pair(false, -1), it)
            }
        }
    }

    return Pair(Pair(false, -1), Pair(false, -1))
}

private fun calcPart1(input: Input): Int {
    var result = 0
    input.forEach { pattern ->
        val hor = checkPatternHorizontal(pattern)
        val ver = checkPatternVertical(pattern)

        result += if (hor.isNotEmpty() && hor.first().first) (hor.first().second + 1) * 100 else 0
        result += if (ver.isNotEmpty() && ver.first().first) ver.first().second + 1 else 0
    }
    return result
}

private fun checkPatternHorizontal(pattern: Pattern): List<Pair<Boolean, Int>> {
    val result = mutableListOf<Pair<Boolean, Int>>()

    for (row0 in 0..<pattern.size - 1) {
        var row1 = row0
        var row2 = row0 + 1
        var size = 0
        var allMatch = true

        loop@ while (row1 >= 0 && row2 < pattern.size) {
            for (col in pattern[0].indices)
                if (pattern[row1][col] != pattern[row2][col]) {
                    allMatch = false
                    break@loop
                }

            row1--
            row2++
            size++
        }

        if (allMatch && (row1 == -1 || row2 == pattern.size))
            result.add(Pair(true, row0))
    }

    return result
}

private fun checkPatternVertical(pattern: Pattern): List<Pair<Boolean, Int>> {
    val result = mutableListOf<Pair<Boolean, Int>>()

    for (col0 in 0..<pattern[0].size - 1) {
        var col1 = col0
        var col2 = col0 + 1
        var size = 0
        var allMatch = true

        loop@ while (col1 >= 0 && col2 < pattern[0].size) {
            for (row in pattern.indices)
                if (pattern[row][col1] != pattern[row][col2]) {
                    allMatch = false
                    break@loop
                }

            col1--
            col2++
            size++
        }

        if (allMatch && (col1 == -1 || col2 == pattern[0].size))
            result.add(Pair(true, col0))
    }

    return result
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    val input: MutableList<List<MutableList<Char>>> = mutableListOf()
    var pattern: MutableList<MutableList<Char>> = mutableListOf()
    var curr = 0

    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) {
            input.add(pattern)
            pattern = mutableListOf()
            curr++
        } else pattern.add(rawLine.toMutableList())
    }

    input.add(pattern)
    return input
}

private typealias Input = List<Pattern>
private typealias Pattern = List<MutableList<Char>>
