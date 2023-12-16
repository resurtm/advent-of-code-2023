package com.resurtm.aoc2023.day16

fun launchDay16(testCase: String) {
    val vis = mutableSetOf<Pos>()
    runBeam(Pos(), Dir.RIGHT, readInputGrid(testCase), vis)
    println(vis.size)
}

private fun runBeam(posInp: Pos, dirInp: Dir, grid: Grid, vis: Vis) {
    var pos = posInp.copy()
    var dir = dirInp
    var counter = 0

    while (counter++ < 20) {
        println(pos)

        if (pos.row < 0 || pos.col < 0 || pos.row > grid.size - 1 || pos.col > grid[0].size - 1) break
        vis.add(pos)

        when (grid[pos.row][pos.col]) {
            '.' -> pos = moveBeam(pos, dir)
            '|', '-' -> {
                val r = branchBeam(pos, dir, grid, vis)
                pos = r.first; dir = r.second
            }

            '/', '\\' -> {
                val r = diagonalBeam(pos, dir, grid[pos.row][pos.col])
                pos = r.first; dir = r.second
            }
        }
    }
}

private fun diagonalBeam(pos: Pos, dir: Dir, tile: Char): Pair<Pos, Dir> {
    if (tile == '/') return when (dir) {
        Dir.TOP -> Pair(pos.copy(col = pos.col + 1), Dir.RIGHT)
        Dir.DOWN -> Pair(pos.copy(col = pos.col - 1), Dir.LEFT)
        Dir.LEFT -> Pair(pos.copy(row = pos.row + 1), Dir.DOWN)
        Dir.RIGHT -> Pair(pos.copy(row = pos.row - 1), Dir.TOP)
    }
    if (tile == '\\') return when (dir) {
        Dir.TOP -> Pair(pos.copy(col = pos.col - 1), Dir.LEFT)
        Dir.DOWN -> Pair(pos.copy(col = pos.col + 1), Dir.RIGHT)
        Dir.LEFT -> Pair(pos.copy(row = pos.row - 1), Dir.TOP)
        Dir.RIGHT -> Pair(pos.copy(row = pos.row + 1), Dir.DOWN)
    }
    throw Exception("Invalid state, cannot process a diagonal move")
}

private fun branchBeam(pos: Pos, dir: Dir, grid: Grid, vis: Vis): Pair<Pos, Dir> {
    val tile = grid[pos.row][pos.col]
    if (tile == '|' && (dir == Dir.LEFT || dir == Dir.RIGHT)) {
        runBeam(pos.copy(row = pos.row - 1), Dir.TOP, grid, vis)
        return Pair(pos.copy(row = pos.row + 1), Dir.DOWN)
    } else if (tile == '-' && (dir == Dir.TOP || dir == Dir.DOWN)) {
        runBeam(pos.copy(col = pos.col - 1), Dir.LEFT, grid, vis)
        return Pair(pos.copy(col = pos.col + 1), Dir.RIGHT)
    }
    return Pair(moveBeam(pos, dir), dir)
}

private fun moveBeam(pos: Pos, dir: Dir): Pos = when (dir) {
    Dir.TOP -> pos.copy(row = pos.row - 1)
    Dir.DOWN -> pos.copy(row = pos.row + 1)
    Dir.LEFT -> pos.copy(col = pos.col - 1)
    Dir.RIGHT -> pos.copy(col = pos.col + 1)
}

private fun printGrid(grid: Grid, pos: List<Pos>) {
    println("==========")
    grid.forEachIndexed { row, rowItems ->
        rowItems.forEachIndexed { col, colItem ->
            val p = Pos(row, col)
            print(if (pos.indexOf(p) == -1) colItem else 'X')
        }
        println()
    }
}

private fun readInputGrid(testCase: String): Grid {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read an input")
    val input = mutableListOf<MutableList<Char>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        input.add(rawLine.trim().toMutableList())
    }
    return input
}

private data class Pos(val row: Int = 0, val col: Int = 0)

private enum class Dir {
    TOP,
    DOWN,
    LEFT,
    RIGHT
}

private typealias Grid = List<List<Char>>

private typealias Vis = MutableSet<Pos>
