package com.resurtm.aoc2023.day23

fun launchDay23(testCase: String) {
    val grid = Grid.readInput(testCase)
    grid.print()
    println(grid.start)
    println(grid.end)
}

private data class Grid(val grid: List<List<Char>>, val start: Pos, val end: Pos) {
    fun print() {
        println("----------")
        for (row in grid.indices) {
            for (col in grid[row].indices)
                print(grid[row][col])
            println()
        }
    }

    companion object {
        internal fun readInput(testCase: String): Grid {
            val reader =
                object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
                    ?: throw Exception("Cannot read an input, probably it is invalid")

            val grid = mutableListOf<List<Char>>()
            while (true) {
                val rawLine = reader.readLine()
                    ?: break
                if (rawLine.trim().isEmpty())
                    continue
                grid.add(rawLine.trim().toList())
            }

            val lastRow = grid.size - 1
            return Grid(
                grid,
                start = Pos(row = 0, col = grid[0].indexOf('.').toLong()),
                end = Pos(row = lastRow.toLong(), col = grid[lastRow].indexOf('.').toLong())
            )
        }
    }
}

private data class Pos(val row: Long, val col: Long)
