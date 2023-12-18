package com.resurtm.aoc2023.day18

import kotlin.math.abs

fun solvePart1(moves: List<Move>): Int {
    val minMax = findMinMax(moves)

    val size = Pos(
        abs(minMax.min.row) + abs(minMax.max.row) + 1,
        abs(minMax.min.col) + abs(minMax.max.col) + 1
    )
    val delta = Pos(-minMax.min.row, -minMax.min.col)
    val grid = Grid(buildGrid(size), delta)

    fillGridBorders(moves, grid)
    printGrid(grid)
    println("Debug: ${countFilled(grid)}")

    fillGridInside(grid)
    println("Debug: ${countFilled(grid)}")

    return countFilled(grid)
}

private fun fillGridInside(g: Grid) {
    val queue = ArrayDeque<Pos>()
    queue.add(findStartPos(g))

    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        val next = findNextPos(curr, g)
        queue.addAll(next)
        g.set(curr, '#')
        next.forEach {
            g.set(it, '#')
        }
    }
}

private fun findNextPos(pos: Pos, g: Grid): List<Pos> {
    val result = mutableListOf<Pos>()
    if (g.get(pos.row - 1, pos.col) != '#')
        result.add(pos.copy(row = pos.row - 1))
    if (g.get(pos.row + 1, pos.col) != '#')
        result.add(pos.copy(row = pos.row + 1))
    if (g.get(pos.row, pos.col - 1) != '#')
        result.add(pos.copy(col = pos.col - 1))
    if (g.get(pos.row, pos.col + 1) != '#')
        result.add(pos.copy(col = pos.col + 1))
    return result
}

private fun findStartPos(g: Grid): Pos {
    var startPos = Pos()
    startPosLoop@ for (row in g.minPos().row..g.maxPos().row) {
        for (col in g.minPos().col..g.maxPos().col) {
            if (g.get(row, col) == '#' && g.get(row + 1, col) == '#' && g.get(row, col + 1) == '#') {
                startPos = Pos(row + 1, col + 1)
                break@startPosLoop
            }
        }
    }
    return startPos
}

private fun countFilled(grid: Grid): Int {
    var count = 0
    for (row in grid.grid.indices) {
        for (col in grid.grid[row].indices) {
            if (grid.grid[row][col] == '#') count++
        }
    }
    return count
}

private fun fillGridBorders(moves: List<Move>, grid: Grid) {
    var curr = Pos()

    for (move in moves) {
        val nextPos = getNextPos(curr, move)

        if (nextPos.row == curr.row) {
            if (nextPos.col > curr.col) {
                for (col in curr.col..nextPos.col) grid.set(curr.row, col, '#')
            } else if (nextPos.col < curr.col) {
                for (col in nextPos.col..curr.col) grid.set(curr.row, col, '#')
            } else throw Exception("Invalid state, zero step moves are not supported")
        } else if (nextPos.col == curr.col) {
            if (nextPos.row > curr.row) {
                for (row in curr.row..nextPos.row) grid.set(row, curr.col, '#')
            } else if (nextPos.row < curr.row) {
                for (row in nextPos.row..curr.row) grid.set(row, curr.col, '#')
            } else throw Exception("Invalid state, zero step moves are not supported")
        } else throw Exception("Invalid state, diagonal moves are not supported")

        curr = nextPos
    }
}

private fun buildGrid(size: Pos): GridData {
    val grid: GridData = mutableListOf()
    repeat(size.row.toInt()) {
        val items = mutableListOf<Char>()
        repeat(size.col.toInt()) { items.add('.') }
        grid.add(items)
    }
    return grid
}

private fun findMinMax(moves: List<Move>): MinMax {
    var curr = Pos()
    var min = Pos()
    var max = Pos()

    for (move in moves) {
        curr = getNextPos(curr, move)

        if (curr.col < min.col) min = min.copy(col = curr.col)
        if (curr.row < min.row) min = min.copy(row = curr.row)
        if (curr.col > max.col) max = max.copy(col = curr.col)
        if (curr.row > max.row) max = max.copy(row = curr.row)
    }

    return MinMax(min, max)
}
