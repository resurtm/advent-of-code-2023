package com.resurtm.aoc2023.day21

fun launchDay21(testCase: String) {
    val part1 = solvePart1(readInput(testCase), 64L).size
    println("Day 21, part 1: $part1")
}

internal fun solvePart1(grid: Grid, maxSteps: Long): Set<Pos> {
    var coords = mutableSetOf(grid.findStart())
    var steps = 0L

    while (steps < maxSteps) {
        val nextCoords = mutableSetOf<Pos>()
        coords.forEach {
            nextCoords.addAll(grid.findNextCoords(it))
        }

        coords = nextCoords
        steps++
    }

    return coords
}

internal data class Grid(val grid: List<List<Char>>) {
    internal fun findStart(): Pos {
        grid.indices.forEach { row ->
            val col = grid[row].indexOf('S')
            if (col != -1)
                return Pos(row.toLong(), col.toLong())
        }
        throw Exception("Cannot find a start, probably an invalid grid")
    }

    fun findNextCoords(pos: Pos): List<Pos> = arrayOf(
        pos.copy(row = pos.row - 1),
        pos.copy(row = pos.row + 1),
        pos.copy(col = pos.col - 1),
        pos.copy(col = pos.col + 1),
    ).mapNotNull {
        if (it.row < 0 || it.row >= grid.size || it.col < 0 || it.col >= grid[0].size)
            null
        else if (grid[it.row.toInt()][it.col.toInt()] == '#')
            null
        else
            it
    }

    fun print(coords: Collection<Pos>? = null) {
        grid.forEachIndexed { row, items ->
            items.forEachIndexed { col, item ->
                if (coords != null && coords.contains(Pos(row.toLong(), col.toLong()))) {
                    print('O')
                } else {
                    print(item)
                }
            }
            println()
        }
    }
}

internal data class Pos(val row: Long = 0L, val col: Long = 0L)

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
    return Grid(grid)
}
