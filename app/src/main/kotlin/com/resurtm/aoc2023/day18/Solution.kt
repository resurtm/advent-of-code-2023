package com.resurtm.aoc2023.day18

import kotlin.math.max
import kotlin.math.min

fun launchDay18(testCase: String) {
    val moves = readMoves(testCase)

    val part1 = solvePart1(moves)
    println("Day 18, part 1: $part1")

    // val part2 = solvePart2(moves)
    // println("Day 18, part 2: $part2")

    val part2good = solvePart2Good(moves)
    println("Day 18, part 2: $part2good")
}

fun getNextPos(pos: Pos, move: Move): Pos = when (move.dir) {
    Dir.U -> pos.copy(row = pos.row - move.len)
    Dir.D -> pos.copy(row = pos.row + move.len)
    Dir.L -> pos.copy(col = pos.col - move.len)
    Dir.R -> pos.copy(col = pos.col + move.len)
}

fun printGrid(g: Grid) {
    println("=====")
    g.grid.forEach { row ->
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
        val len = rawParts[1].toLong()
        val color = rawParts[2].trimStart('(', '#').trimEnd(')')

        moves.add(Move(dir, len, color))
    }

    return moves
}

data class Grid(val grid: GridData, val delta: Pos) {
    fun set(row: Long, col: Long, ch: Char) {
        grid[(row + delta.row).toInt()][(col + delta.col).toInt()] = ch
    }

    fun set(pos: Pos, ch: Char) {
        set(pos.row, pos.col, ch)
    }

    fun get(row: Long, col: Long): Char {
        return grid[(row + delta.row).toInt()][(col + delta.col).toInt()]
    }

    fun minPos(): Pos {
        return Pos(-delta.row, -delta.col)
    }

    fun maxPos(): Pos {
        return Pos(grid.size - delta.row, grid[0].size - delta.col)
    }
}

typealias GridData = MutableList<MutableList<Char>>

data class MinMax(val min: Pos, val max: Pos)

data class Move(val dir: Dir, val len: Long, val color: String)

data class Pos(val row: Long = 0L, val col: Long = 0L) {
    override fun toString(): String = "row=$row/col=$col"

    fun isInside(minMax: MinMax): Boolean {
        return (this.row in minMax.min.row..minMax.max.row) &&
                (this.col in minMax.min.col..minMax.max.col)
    }
}

enum class Dir { U, D, L, R }

data class Rect(
    val minMax: MinMax,
    val isCw: Boolean = false,
    val isCcw: Boolean = false,
    val linesInMinMax: Long = 0L,
    var checked: Boolean = false,
    val lines: List<Line> = emptyList(),
) {
    val area = ((minMax.max.row - minMax.min.row) + 1) * ((minMax.max.col - minMax.min.col) + 1)

    fun isOtherInside(other: Rect): Boolean {
        return (this.minMax.min.row <= other.minMax.min.row && this.minMax.min.col <= other.minMax.min.col)
                && (this.minMax.max.row >= other.minMax.max.row && this.minMax.max.col >= other.minMax.max.col)
    }
}

fun findCommonArea(a: Rect, other: Rect, small: Boolean = false): Long {
    val minMax = findCommonMinMax(a, other)
    if (small)
        return ((minMax.max.row- minMax.min.row) - 1) * ((minMax.max.col - minMax.min.col) - 1)
    return ((minMax.max.row - minMax.min.row) + 1) * ((minMax.max.col - minMax.min.col) + 1)
}

/*fun findCommonArea(a: MinMax, other: MinMax): Long {
    val minMax = findCommonMinMax(a, other)
    return ((minMax.max.row - minMax.min.row) + 1) * ((minMax.max.col - minMax.min.col) + 1)
}*/

fun findCommonMinMax(a: Rect, other: Rect): MinMax {
    val row0 = max(a.minMax.min.row, other.minMax.min.row)
    val col0 = max(a.minMax.min.col, other.minMax.min.col)
    val row1 = min(a.minMax.max.row, other.minMax.max.row)
    val col1 = min(a.minMax.max.col, other.minMax.max.col)
    return MinMax(Pos(row0, col0), Pos(row1, col1))
}

fun findCommonMinMax(a: MinMax, other: MinMax): MinMax {
    val row0 = max(a.min.row, other.min.row)
    val col0 = max(a.min.col, other.min.col)
    val row1 = min(a.max.row, other.max.row)
    val col1 = min(a.max.col, other.max.col)
    return MinMax(Pos(row0, col0), Pos(row1, col1))
}

data class Line(val head: Pos, val tail: Pos) {
    val start: Pos
    val end: Pos
    val length: Long

    init {
        val cmp = head.row < tail.row || head.col < tail.col
        this.start = if (cmp) head else tail
        this.end = if (cmp) tail else head

        this.length = if (start.row == end.row) (end.col - start.col + 1) else (end.row - start.row + 1)
    }

    fun intersects(line: Line): Boolean {
        val c1 = (this.start.row in line.start.row..line.end.row) &&
                (line.start.col in this.start.col..this.end.col)
        val c2 = (line.start.row in this.start.row..this.end.row) &&
                (this.start.col in line.start.col..line.end.col)
        return c1 || c2
    }

    override fun toString(): String = "start($start) & end($end) & len($length)"
}
