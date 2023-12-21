package com.resurtm.aoc2023.day21

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
