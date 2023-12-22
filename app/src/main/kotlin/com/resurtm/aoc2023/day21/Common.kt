package com.resurtm.aoc2023.day21

internal data class Grid(val grid: List<List<Char>>) {
    internal fun traverse(maxSteps: Long, useInf: Boolean = false): TraverseResult {
        var coords = mutableSetOf(findStart())
        var steps = 0L

        while (steps < maxSteps) {
            val nextCoords = mutableSetOf<Pos>()
            coords.forEach {
                nextCoords.addAll(
                    if (useInf) findNextCoordsInf(it)
                    else findNextCoords(it)
                )
            }

            coords = nextCoords
            steps++
        }

        return TraverseResult(coords, rows = grid.size.toLong(), cols = grid[0].size.toLong())
    }

    private fun findStart(): Pos {
        grid.indices.forEach { row ->
            val col = grid[row].indexOf('S')
            if (col != -1)
                return Pos(row.toLong(), col.toLong())
        }
        throw Exception("Cannot find a start, probably an invalid grid")
    }

    private fun findNextCoordsInf(pos: Pos): List<Pos> = findCrossCombs(pos)
        .mapNotNull {
            if (getInf(it) == '#')
                null
            else
                it
        }

    private fun findNextCoords(pos: Pos): List<Pos> = findCrossCombs(pos)
        .mapNotNull {
            if (it.row < 0 || it.row >= grid.size || it.col < 0 || it.col >= grid[0].size)
                null
            else if (grid[it.row.toInt()][it.col.toInt()] == '#')
                null
            else
                it
        }

    private fun getInf(pos: Pos): Char {
        var row = pos.row.toInt() % grid.size
        var col = pos.col.toInt() % grid[0].size

        if (row < 0) row += grid.size
        if (col < 0) col += grid[0].size

        return grid[row][col]
    }

    private fun findCrossCombs(pos: Pos): Array<Pos> = arrayOf(
        pos.copy(row = pos.row - 1),
        pos.copy(row = pos.row + 1),
        pos.copy(col = pos.col - 1),
        pos.copy(col = pos.col + 1),
    )

    internal fun print(coords: Collection<Pos>? = null, addSeparator: Boolean = true) {
        if (addSeparator)
            println("--------------------")
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

internal data class TraverseResult(
    val coords: Set<Pos>,
    val rows: Long,
    val cols: Long,
) {
    val clampedCoords: Set<Pos> = coords.filter {
        it.row >= 0 && it.col >= 0 && it.row < rows && it.col < cols
    }.toSet()
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

internal fun findCoeffs(p0: Pos, p1: Pos, p2: Pos): Coeffs {
    val x1 = p0.row.toDouble()
    val x2 = p1.row.toDouble()
    val x3 = p2.row.toDouble()
    val y1 = p0.col.toDouble()
    val y2 = p1.col.toDouble()
    val y3 = p2.col.toDouble()

    val a = y1 / ((x1 - x2) * (x1 - x3)) + y2 / ((x2 - x1) * (x2 - x3)) + y3 / ((x3 - x1) * (x3 - x2))

    val b = (-y1 * (x2 + x3) / ((x1 - x2) * (x1 - x3))
            - y2 * (x1 + x3) / ((x2 - x1) * (x2 - x3))
            - y3 * (x1 + x2) / ((x3 - x1) * (x3 - x2)))

    val c = (y1 * x2 * x3 / ((x1 - x2) * (x1 - x3))
            + y2 * x1 * x3 / ((x2 - x1) * (x2 - x3))
            + y3 * x1 * x2 / ((x3 - x1) * (x3 - x2)))

    return Coeffs(a, b, c)
}

data class Coeffs(val a: Double, val b: Double, val c: Double)
