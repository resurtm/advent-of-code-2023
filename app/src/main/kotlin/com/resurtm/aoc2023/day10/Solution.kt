package com.resurtm.aoc2023.day10

fun launchDay10(testCase: String) {
    val env = readEnv(testCase)
    ensureStart(env)

    val hist = Pair(mutableSetOf(env.pos), mutableSetOf(env.pos))
    val pos = findStarts(env)

    var step = 0
    do {
        println(step)
    } while (++step < 3)
}

private fun findStarts(e: Env): Pair<Pos, Pos> {
    val p = e.pos
    val m = e.grid[p.row][p.col]

    if (m == Pipe.WEST_EAST) return Pair(p.copy(col = p.col - 1), p.copy(col = p.col + 1))
    if (m == Pipe.NORTH_SOUTH) return Pair(p.copy(col = p.col - 1), p.copy(col = p.col + 1))

    if (m == Pipe.NORTH_WEST) return Pair(p.copy(row = p.row - 1), p.copy(col = p.col - 1))
    if (m == Pipe.SOUTH_WEST) return Pair(p.copy(row = p.row + 1), p.copy(col = p.col - 1))

    if (m == Pipe.NORTH_EAST) return Pair(p.copy(row = p.row - 1), p.copy(col = p.col + 1))
    if (m == Pipe.SOUTH_EAST) return Pair(p.copy(row = p.row + 1), p.copy(col = p.col + 1))

    throw Exception("Invalid state, cannot find the pair of starts")
}

private fun ensureStart(e: Env) {
    val north = if (e.pos.row == 0) Pipe.BLANK else e.grid[e.pos.row - 1][e.pos.col]
    val south = if (e.pos.row == e.grid.size - 1) Pipe.BLANK else e.grid[e.pos.row + 1][e.pos.col]

    val west = if (e.pos.col == 0) Pipe.BLANK else e.grid[e.pos.row][e.pos.col - 1]
    val east = if (e.pos.col == e.grid[0].size) Pipe.BLANK else e.grid[e.pos.row][e.pos.col + 1]

    e.grid[e.pos.row][e.pos.col] = ensureStartEx(north, south, west, east)
}

private fun ensureStartEx(north: Pipe, south: Pipe, west: Pipe, east: Pipe): Pipe {
    val n = north in arrayOf(Pipe.NORTH_SOUTH, Pipe.SOUTH_WEST, Pipe.SOUTH_EAST)
    val s = south in arrayOf(Pipe.NORTH_SOUTH, Pipe.NORTH_WEST, Pipe.NORTH_EAST)

    val w = west in arrayOf(Pipe.WEST_EAST, Pipe.NORTH_EAST, Pipe.SOUTH_EAST)
    val e = east in arrayOf(Pipe.WEST_EAST, Pipe.NORTH_WEST, Pipe.SOUTH_WEST)

    if (n && s) return Pipe.NORTH_SOUTH
    if (w && e) return Pipe.WEST_EAST

    if (n && w) return Pipe.NORTH_WEST
    if (n && e) return Pipe.NORTH_EAST

    if (s && w) return Pipe.SOUTH_WEST
    if (s && e) return Pipe.SOUTH_EAST

    throw Exception("Invalid state, cannot ensure the start")
}

private fun readEnv(testCase: String): Env {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    val result = Env(pos = Pos(-1, -1))
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
        fun fromChar(v: Char) = values.firstOrNull { it.v == v.code.toByte() }
            ?: throw Exception("Invalid state, cannot parse a pipe")
    }
}

private data class Pos(val row: Int = 0, val col: Int = 0)

private data class Env(val grid: Grid = mutableListOf(), var pos: Pos = Pos())

private typealias Grid = MutableList<MutableList<Pipe>>
