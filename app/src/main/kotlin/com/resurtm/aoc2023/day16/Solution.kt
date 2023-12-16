package com.resurtm.aoc2023.day16

fun launchDay16(testCase: String) {
    val grid = readInputGrid(testCase)

    val part1 = runBeam(grid, Pos(), Dir.RIGHT).size
    println("Day 16, part 1: $part1")

    val part2 = runBeams(grid)
    println("Day 16, part 2: $part2")
}

private fun runBeams(gr: Grid): Int {
    var max = 0
    for (col in gr[0].indices) {
        val v1 = runBeam(gr, Pos(0, col), Dir.DOWN).size
        if (max < v1) max = v1
        val v2 = runBeam(gr, Pos(gr.size - 1, col), Dir.UP).size
        if (max < v2) max = v2
    }
    for (row in gr.indices) {
        val v1 = runBeam(gr, Pos(row, 0), Dir.RIGHT).size
        if (max < v1) max = v1
        val v2 = runBeam(gr, Pos(row, gr[0].size - 1), Dir.LEFT).size
        if (max < v2) max = v2
    }
    return max
}

private fun runBeam(gr: Grid, startPos: Pos, startDir: Dir): Set<Pos> {
    val bms = mutableListOf(Beam(startPos, startDir))
    val vis = mutableSetOf(bms.first())
    val vh = mutableListOf<Int>()
    val vhs = 10

    var genNum = 0
    while (!(vh.size >= vhs && vh.subList(vh.size - vhs, vh.size).all { it == vh.last() })) {
        /*printGrid(genNum, gr, bms)
        printVis(genNum, gr, vis)*/

        var bmNum = 0
        while (bmNum < bms.size) {
            val bm = bms[bmNum]
            var p = bm.p
            var d = bm.d

            if (p.row >= 0 && p.col >= 0 && p.row < gr.size && p.col < gr[0].size) when (gr[p.row][p.col]) {
                '.' -> p = moveBeam(p, d)

                '|', '-' -> {
                    val r = branchBeam(p, d, gr)
                    p = r.first.p
                    d = r.first.d

                    val second = r.second
                    if (second != null) {
                        bmNum++
                        bms.addFirst(second)
                        vis.add(second)
                    }
                }

                '/', '\\' -> {
                    val r = diagonalBeam(p, d, gr[p.row][p.col])
                    p = r.p
                    d = r.d
                }
            }

            val bmn = Beam(p, d)
            if (bmn in vis || p.row < 0 || p.col < 0 || p.row >= gr.size || p.col >= gr[0].size) {
                bms.removeAt(bmNum)
                continue
            }

            bms[bmNum] = bmn
            vis.add(bmn)
            bmNum++
        }

        // println("${vis.size} - ${bms.size} - $genNum")
        vh.add(vis.size)
        genNum++
    }

    return vis.map { it.p }.toSet()
}

private fun diagonalBeam(p: Pos, d: Dir, t: Char): Beam {
    if (t == '/') return when (d) {
        Dir.UP -> Beam(p.copy(col = p.col + 1), Dir.RIGHT)
        Dir.DOWN -> Beam(p.copy(col = p.col - 1), Dir.LEFT)
        Dir.LEFT -> Beam(p.copy(row = p.row + 1), Dir.DOWN)
        Dir.RIGHT -> Beam(p.copy(row = p.row - 1), Dir.UP)
    }
    if (t == '\\') return when (d) {
        Dir.UP -> Beam(p.copy(col = p.col - 1), Dir.LEFT)
        Dir.DOWN -> Beam(p.copy(col = p.col + 1), Dir.RIGHT)
        Dir.LEFT -> Beam(p.copy(row = p.row - 1), Dir.UP)
        Dir.RIGHT -> Beam(p.copy(row = p.row + 1), Dir.DOWN)
    }
    throw Exception("Invalid state, cannot process a diagonal move")
}

private fun branchBeam(p: Pos, d: Dir, gr: Grid): Pair<Beam, Beam?> {
    if (gr[p.row][p.col] == '|' && (d == Dir.LEFT || d == Dir.RIGHT)) {
        return Pair(
            Beam(p.copy(row = p.row - 1), Dir.UP),
            Beam(p.copy(row = p.row + 1), Dir.DOWN)
        )
    }
    if (gr[p.row][p.col] == '-' && (d == Dir.UP || d == Dir.DOWN)) {
        return Pair(
            Beam(p.copy(col = p.col - 1), Dir.LEFT),
            Beam(p.copy(col = p.col + 1), Dir.RIGHT)
        )
    }
    return Pair(Beam(moveBeam(p, d), d), null)
}

private fun moveBeam(p: Pos, d: Dir): Pos = when (d) {
    Dir.UP -> p.copy(row = p.row - 1)
    Dir.DOWN -> p.copy(row = p.row + 1)
    Dir.LEFT -> p.copy(col = p.col - 1)
    Dir.RIGHT -> p.copy(col = p.col + 1)
}

private fun printGrid(gen: Int, gr: Grid, beams: List<Beam>) {
    println("Generation $gen")
    val pos = beams.map { it.p }
    gr.forEachIndexed { row, rowItems ->
        rowItems.forEachIndexed { col, colItem ->
            val p = Pos(row, col)
            print(if (pos.indexOf(p) == -1) colItem else 'X')
        }
        println()
    }
}

private fun printVis(gen: Int, gr: Grid, vis: Set<Beam>) {
    println("Generation $gen")
    val pos = vis.map { it.p }
    gr.forEachIndexed { row, rowItems ->
        rowItems.forEachIndexed { col, _ ->
            val p = Pos(row, col)
            print(if (p in pos) '#' else '.')
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
    UP,
    DOWN,
    LEFT,
    RIGHT
}

private data class Beam(val p: Pos, val d: Dir)

private typealias Grid = List<List<Char>>
