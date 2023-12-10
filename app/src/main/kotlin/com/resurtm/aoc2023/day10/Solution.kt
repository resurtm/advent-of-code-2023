package com.resurtm.aoc2023.day10

fun launchDay10(testCase: String) {
    val input = readInput(testCase)
    println(input.pos)
    for (row in input.grid) {
        for (item in row) {
            print("${item.v} ")
        }
        println()
    }
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read the input")
    val result = Input(pos = Pos(-1, -1))
    var row = 0
    while (true) {
        val rawLine = reader.readLine() ?: break
        result.grid.add(rawLine.mapIndexed { col, it ->
            val pipe = Pipe.fromChar(it)
            if (pipe == Pipe.START) result.pos = Pos(row, col)
            pipe
        }.toMutableList())
        row++
    }
    return result
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

private data class Pos(val row: Int = 0, val col: Int = 0)

private data class Input(val grid: Grid = mutableListOf(), var pos: Pos = Pos())

private typealias Grid = MutableList<MutableList<Pipe>>
