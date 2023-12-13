package com.resurtm.aoc2023.day13

fun launchDay13(testCase: String) {
    val input = readInput(testCase)
    println("Day 13, part 1: ${calcPart1(input)}")
}

private fun calcPart1(input: Input): Int {
    var result = 0
    input.forEach { pattern ->
        val hor = checkPatternHorizontal(pattern)
        val ver = checkPatternVertical(pattern)
        // println(if (hor.first >= ver.first) hor.second else ver.second)
        result += if (hor.first >= ver.first) hor.second * 100 else ver.second
    }
    return result
}

private fun checkPatternHorizontal(pattern: Pattern): Pair<Int, Int> {
    var maxSize = 0
    var maxRow = 0

    for (row0 in 0..<pattern.size - 1) {
        var row1 = row0
        var row2 = row0 + 1
        var size = 0

        loop@ while (row1 >= 0 && row2 < pattern.size) {
            for (col in pattern[0].indices)
                if (pattern[row1][col] != pattern[row2][col])
                    break@loop

            row1--
            row2++
            size++
        }

        if (maxSize < size) {
            maxSize = size
            maxRow = row0
        }
    }

    return Pair(maxSize, maxRow + 1)
}

private fun checkPatternVertical(pattern: Pattern): Pair<Int, Int> {
    var maxSize = 0
    var maxCol = 0

    for (col0 in 0..<pattern[0].size - 1) {
        var col1 = col0
        var col2 = col0 + 1
        var size = 0

        loop@ while (col1 >= 0 && col2 < pattern[0].size) {
            for (row in pattern.indices)
                if (pattern[row][col1] != pattern[row][col2])
                    break@loop

            col1--
            col2++
            size++
        }

        if (maxSize < size) {
            maxSize = size
            maxCol = col0
        }
    }

    return Pair(maxSize, maxCol + 1)
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    val input: MutableList<List<List<Char>>> = mutableListOf()
    var pattern: MutableList<List<Char>> = mutableListOf()
    var curr = 0

    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) {
            input.add(pattern)
            pattern = mutableListOf()
            curr++
        } else pattern.add(rawLine.toList())
    }

    input.add(pattern)
    return input
}

private typealias Input = List<Pattern>
private typealias Pattern = List<List<Char>>
