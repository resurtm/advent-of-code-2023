package com.resurtm.aoc2023.day18

import kotlin.math.abs

fun launchDay18(testCase: String) {
    solvePart1(readMoves(testCase))
}

fun solvePart1(moves: List<Move>) {
    val minMax = findMinMax(moves)

    val size = Pos(abs(minMax.min.row) + abs(minMax.max.row), abs(minMax.min.col) + abs(minMax.max.col))
    val delta = Pos(-minMax.min.row, -minMax.min.col)

    val grid: Grid = mutableListOf()
    repeat(size.row) {
        val items = mutableListOf<Char>()
        repeat(size.col) { items.add('.') }
        grid.add(items)
    }
    printGrid(grid)
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

private fun getNextPos(pos: Pos, move: Move): Pos = when (move.dir) {
    Dir.U -> pos.copy(row = pos.row - move.len)
    Dir.D -> pos.copy(row = pos.row + move.len)
    Dir.L -> pos.copy(col = pos.col + move.len)
    Dir.R -> pos.copy(col = pos.col + move.len)
}

private fun printGrid(grid: Grid) {
    println("=====")
    grid.forEach { row ->
        row.forEach { print(it) }
        println()
    }
}

fun readMoves(testCase: String): List<Move> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read an input")

    val moves = mutableListOf<Move>()

    while (true) {
        val rawLine = reader.readLine() ?: break
        val rawParts = rawLine.trim().split(' ')

        val dir = when (rawParts[0][0]) {
            'U' -> Dir.U
            'D' -> Dir.D
            'L' -> Dir.L
            'R' -> Dir.R
            else -> throw Exception("Invalid state, an unknown direction")
        }
        val len = rawParts[1].toInt()
        val color = rawParts[2].trimStart('(', '#').trimEnd(')')

        moves.add(Move(dir, len, color))
    }

    return moves
}

typealias Grid = MutableList<MutableList<Char>>

data class MinMax(val min: Pos, val max: Pos)

data class Move(val dir: Dir, val len: Int, val color: String)

data class Pos(val row: Int = 0, val col: Int = 0)

enum class Dir { U, D, L, R }
