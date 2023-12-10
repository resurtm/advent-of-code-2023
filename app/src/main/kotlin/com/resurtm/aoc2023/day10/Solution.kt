package com.resurtm.aoc2023.day10

import kotlin.math.abs

fun launchDay10(testCase: String) {
    val env = readEnv(testCase)
    ensureStart(env)
    println("Day 10, part 1: ${calcPart1(env)}")
    // println("Day 10, part 2: ${calcPart2(env)}")
    println("Day 10, part 2: ${calcPart2V2(env)}")
}

private fun calcPart2V2(env: Env): Int {
    val walked = calcWalkedSet(env)
    val marked = mutableSetOf<Pos>()

    for (row in 0..<env.grid.size) {
        for (col in 0..<env.grid.first().size) {
            val pos = Pos(row, col)
            if (pos in walked) continue

            val markedAddition = mutableSetOf<Pos>()
            if (emit(pos, markedAddition, walked, env.grid))
                marked.addAll(markedAddition)
        }
    }

    println(marked)

    return marked.size
}

private fun emit(pos: Pos, accum: MutableSet<Pos>, walked: Set<Pos>, grid: Grid): Boolean {
    if (pos.row <= -1 || pos.col <= -1 || pos.row >= grid.size || pos.col >= grid.first().size) {
        accum.clear()
        return false
    }

    if (pos in walked || pos in accum) {
        return true
    }
    accum.add(pos)

    val north = pos.copy(row = pos.row - 1)
    val south = pos.copy(row = pos.row + 1)
    val west = pos.copy(col = pos.col - 1)
    val east = pos.copy(col = pos.col + 1)

    if (!emit(north, accum, walked, grid)) return false
    if (!emit(south, accum, walked, grid)) return false
    if (!emit(west, accum, walked, grid)) return false
    if (!emit(east, accum, walked, grid)) return false

    return true
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

private fun calcPart2(env: Env): Int {
    val hist: HistV2 = mutableListOf(env.pos)
    var pos = findStarts(env).first
    val marked = mutableSetOf<Pos>()

    var step = 0
    do {
        for (item in hist) if (isClose(item, pos) && item != pos && item != hist.last()) {
            val emitStart = findEmitStart(pos, findDirection(pos, hist, env.grid), hist)
            if (emitStart != null) {
                // println("emit - emit:$emitStart - pos:$pos")
                val markedCurr = mutableSetOf<Pos>()
                if (emit(
                        emitStart,
                        (hist + mutableListOf(pos)).toMutableList(),
                        marked,
                        markedCurr,
                        env.grid,
                        depth = 0
                    )
                ) {
                    marked.addAll(markedCurr)
                }
                println(marked)
            }
        }

        hist.add(pos)
        pos = walk(pos, hist.toMutableSet(), env.grid)

        step++
    } while (/*++step < 460 && */!isClose(env.pos, pos))

    return marked.size
}

private fun emit(
    emitStart: Pos,
    hist: HistV2,
    markedAll: MutableSet<Pos>,
    markedCurr: MutableSet<Pos>,
    grid: Grid,
    depth: Int
): Boolean {
    markedCurr.add(emitStart)

    val north = emitStart.copy(row = emitStart.row - 1)
    val south = emitStart.copy(row = emitStart.row + 1)
    val west = emitStart.copy(col = emitStart.col - 1)
    val east = emitStart.copy(col = emitStart.col + 1)

    //println(markedCurr)
    if (
    /*depth > 4 ||*/
        north.row < 0 || west.col < 0 || south.row > grid.size || east.col > grid.first().size
    ) {
        markedCurr.clear()
        return false
    }

    var result = true
    if (result && hist.indexOf(north) == -1 && north !in markedAll && north !in markedCurr)
        result = emit(north, hist, markedAll, markedCurr, grid, depth + 1) && result
    if (result && hist.indexOf(south) == -1 && south !in markedAll && south !in markedCurr)
        result = emit(south, hist, markedAll, markedCurr, grid, depth + 1) && result
    if (result && hist.indexOf(west) == -1 && west !in markedAll && west !in markedCurr)
        result = emit(west, hist, markedAll, markedCurr, grid, depth + 1) && result
    if (result && hist.indexOf(east) == -1 && east !in markedAll && east !in markedCurr)
        result = emit(east, hist, markedAll, markedCurr, grid, depth + 1) && result
    return result
}

private fun findEmitStart(pos: Pos, direction: Direction, hist: HistV2): Pos? {
    val emitStart1 = when (direction) {
        Direction.NORTH -> pos.copy(col = pos.col - 1)
        Direction.SOUTH -> pos.copy(col = pos.col + 1)
        Direction.WEST -> pos.copy(row = pos.row + 1)
        Direction.EAST -> pos.copy(row = pos.row - 1)
    }
    val emitStart2 = when (direction) {
        Direction.NORTH -> pos.copy(row = pos.row + 1, col = pos.col - 1)
        Direction.SOUTH -> pos.copy(row = pos.row - 1, col = pos.col + 1)
        Direction.WEST -> pos.copy(row = pos.row + 1, col = pos.col + 1)
        Direction.EAST -> pos.copy(row = pos.row - 1, col = pos.col - 1)
    }

    if (hist.indexOf(emitStart1) == -1) return emitStart1
    if (hist.indexOf(emitStart2) == -1) return emitStart2
    return null
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
    } while (/*++step < 10 && */hist.first.intersect(hist.second).size == 1)

    return step
}

private fun findDirection(p: Pos, h: HistV2, g: Grid): Direction {
    val prev = h.last()
    if (g[p.row][p.col] == Pipe.NORTH_SOUTH)
        return if (p.copy(row = p.row - 1) == prev) Direction.SOUTH else Direction.NORTH
    if (g[p.row][p.col] == Pipe.WEST_EAST)
        return if (p.copy(col = p.col - 1) == prev) Direction.EAST else Direction.WEST
    if (p.copy(row = p.row - 1) == prev)
        return Direction.SOUTH
    if (p.copy(row = p.row + 1) == prev)
        return Direction.NORTH
    if (p.copy(col = p.col - 1) == prev)
        return Direction.EAST
    if (p.copy(col = p.col + 1) == prev)
        return Direction.WEST
    throw Exception("Invalid state, cannot find a direction")
}

private fun isClose(a: Pos, b: Pos): Boolean {
    /*if (a.row == b.row) return abs(a.col - b.col) <= 1
    if (a.col == b.col) return abs(a.row - b.row) <= 1*/
    return abs(a.col - b.col) + abs(a.row - b.row) <= 1
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

private enum class Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;
}

private data class Pos(val row: Int = 0, val col: Int = 0)

private data class Env(val grid: Grid = mutableListOf(), var pos: Pos = Pos())

private typealias Grid = MutableList<MutableList<Pipe>>

private typealias Hist = MutableSet<Pos>

private typealias HistV2 = MutableList<Pos>
