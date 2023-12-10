package com.resurtm.aoc2023.day10

fun launchDay10(testCase: String) {
    val env = readEnv(testCase)
    ensureStart(env)
    println("Day 10, part 1: ${calcPart1(env)}")
    println("Day 10, part 2: ${calcPart2(env)}")
}

private fun calcPart2(env: Env): Int {
    val flood = cleanDebris(env)
    for (row in 0..<env.grid.size) {
        var fill = false
        for (col in 0..<env.grid.first().size) {
            val x = env.grid[row][col].x
            if (x == '-') {
                continue
            }
            if (x == '.' && fill) {
                flood[row][col] = true
                continue
            }
            if (x == '|' || x == 'F' || x == '7') {
                fill = !fill
                continue
            }
        }
    }
    return flood.fold(0) { acc, row -> acc + row.filter { it }.size }
}

private fun cleanDebris(env: Env): MutableList<MutableList<Boolean>> {
    val walked = calcWalkedSet(env)
    val flood = mutableListOf<MutableList<Boolean>>()
    for (row in 0..<env.grid.size) {
        val items = mutableListOf<Boolean>()
        for (col in 0..<env.grid.first().size) {
            items.add(false)
            val pos = Pos(row, col)
            if (pos in walked) continue
            env.grid[row][col] = Pipe.BLANK
        }
        flood.add(items)
    }
    return flood
}

private fun calcWalkedSet(env: Env): Set<Pos> {
    val hist = Pair(mutableSetOf(env.pos), mutableSetOf(env.pos))
    var pos = findStarts(env)
    do {
        hist.first.add(pos.first)
        hist.second.add(pos.second)
        pos = Pair(
            walk(pos.first, hist.first, env.grid),
            walk(pos.second, hist.second, env.grid)
        )
    } while (hist.first.intersect(hist.second).size == 1)
    return hist.first + hist.second
}

private fun calcPart1(env: Env): Int {
    val hist = Pair(mutableSetOf(env.pos), mutableSetOf(env.pos))
    var pos = findStarts(env)

    var step = 0
    do {
        hist.first.add(pos.first)
        hist.second.add(pos.second)

        pos = Pair(
            walk(pos.first, hist.first, env.grid),
            walk(pos.second, hist.second, env.grid)
        )

        step++
    } while (hist.first.intersect(hist.second).size == 1)

    return step
}

private fun walk(p: Pos, h: Hist, g: Grid): Pos {
    val d = g[p.row][p.col]

    return when (d) {
        Pipe.WEST_EAST -> {
            val p1 = p.copy(col = p.col - 1)
            val p2 = p.copy(col = p.col + 1)
            if (p1 in h) p2 else p1
        }

        Pipe.NORTH_SOUTH -> {
            val p1 = p.copy(row = p.row - 1)
            val p2 = p.copy(row = p.row + 1)
            if (p1 in h) p2 else p1
        }

        Pipe.NORTH_WEST -> {
            val p1 = p.copy(row = p.row - 1)
            val p2 = p.copy(col = p.col - 1)
            if (p1 in h) p2 else p1
        }

        Pipe.SOUTH_WEST -> {
            val p1 = p.copy(row = p.row + 1)
            val p2 = p.copy(col = p.col - 1)
            if (p1 in h) p2 else p1
        }

        Pipe.NORTH_EAST -> {
            val p1 = p.copy(row = p.row - 1)
            val p2 = p.copy(col = p.col + 1)
            if (p1 in h) p2 else p1
        }

        Pipe.SOUTH_EAST -> {
            val p1 = p.copy(row = p.row + 1)
            val p2 = p.copy(col = p.col + 1)
            if (p1 in h) p2 else p1
        }

        else -> throw Exception("Invalid state, cannot progress/walk a position")
    }
}

private fun findStarts(e: Env): Pair<Pos, Pos> {
    val p = e.pos
    val d = e.grid[p.row][p.col]

    if (d == Pipe.WEST_EAST) return Pair(p.copy(col = p.col - 1), p.copy(col = p.col + 1))
    if (d == Pipe.NORTH_SOUTH) return Pair(p.copy(row = p.row - 1), p.copy(row = p.row + 1))

    if (d == Pipe.NORTH_WEST) return Pair(p.copy(row = p.row - 1), p.copy(col = p.col - 1))
    if (d == Pipe.SOUTH_WEST) return Pair(p.copy(row = p.row + 1), p.copy(col = p.col - 1))

    if (d == Pipe.NORTH_EAST) return Pair(p.copy(row = p.row - 1), p.copy(col = p.col + 1))
    if (d == Pipe.SOUTH_EAST) return Pair(p.copy(row = p.row + 1), p.copy(col = p.col + 1))

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

private enum class Pipe(val v: Byte, val x: Char) {
    NORTH_SOUTH('|'.code.toByte(), '|'), // 124
    WEST_EAST('-'.code.toByte(), '-'), // 45
    NORTH_EAST('L'.code.toByte(), 'L'), // 76
    NORTH_WEST('J'.code.toByte(), 'J'), // 74
    SOUTH_WEST('7'.code.toByte(), '7'), // 55
    SOUTH_EAST('F'.code.toByte(), 'F'), // 70
    BLANK('.'.code.toByte(), '.'), // 46
    START('S'.code.toByte(), 'S'); // 83

    companion object {
        private val values = entries
        fun fromChar(v: Char) = values.firstOrNull { it.v == v.code.toByte() }
            ?: throw Exception("Invalid state, cannot parse a pipe")
    }
}

private data class Pos(val row: Int = 0, val col: Int = 0)

private data class Env(val grid: Grid = mutableListOf(), var pos: Pos = Pos())

private typealias Grid = MutableList<MutableList<Pipe>>

private typealias Hist = MutableSet<Pos>
