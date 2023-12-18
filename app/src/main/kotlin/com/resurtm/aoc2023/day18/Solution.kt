package com.resurtm.aoc2023.day18

fun launchDay18(testCase: String) {
    val moves = readMoves(testCase)

     val part1 = solvePart1(moves)
    // println("Day 18, part 1: $part1")

    val part2 = solvePart2(moves)
    // println("Day 18, part 2: $part2")
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
}

enum class Dir { U, D, L, R }
