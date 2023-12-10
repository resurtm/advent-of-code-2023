package com.resurtm.aoc2023.day10

fun launchDay10(testCase: String) {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read the input")
    val grid = mutableListOf<MutableList<Pipe>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        grid.add(rawLine.map { Pipe.fromChar(it) }.toMutableList())
    }
    for (row in grid) {
        for (item in row) {
            print("${item.v} ")
        }
        println()
    }
}

private enum class Pipe(val v: Byte) {
    NORTH_SOUTH('|'.code.toByte()), // 124
    WEST_EAST('-'.code.toByte()), // 45
    NORTH_EAST('L'.code.toByte()), // 76
    NORTH_WEST('J'.code.toByte()), // 74
    SOUTH_WEST('7'.code.toByte()), // 55
    SOUTH_EAST('F'.code.toByte()), // 70
    BLANK('.'.code.toByte()), // 46
    START('S'.code.toByte()); // 83

    companion object {
        private val values = entries
        fun fromChar(v: Char) = values.firstOrNull { it.v == v.code.toByte() } ?: BLANK
    }
}
