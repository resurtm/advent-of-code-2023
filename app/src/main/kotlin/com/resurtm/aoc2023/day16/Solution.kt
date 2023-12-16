package com.resurtm.aoc2023.day16

fun launchDay16(testCase: String) {
    val part1 = runBeam(readInputGrid(testCase))
    println("Day 16, part 1: $part1")
}

private fun runBeam(gr: Grid): Int {
    val beams = mutableListOf(Beam(Pos(), Dir.RIGHT))
    val vis = mutableSetOf<Pos>()

    val his = mutableListOf<Int>()
    val hisSize = 100

    var genCnt = 0
    while (!(his.size >= hisSize && his.subList(his.size - 10, his.size).all { it == his.last() })) {
        // printGrid(genCnt, gr, beams)

        var beamCnt = 0
        while (beamCnt < beams.size) {
            var p = beams[beamCnt].p
            var d = beams[beamCnt].d

            if (p.row < 0 || p.col < 0 || p.row > gr.size - 1 || p.col > gr[0].size - 1) {
                beams.removeAt(beamCnt)
                continue
            }
            vis.add(p)

            when (gr[p.row][p.col]) {
                '.' -> p = moveBeam(p, d)

                '|', '-' -> {
                    val r = branchBeam(p, d, gr)
                    p = r.first.p; d = r.first.d
                    if (r.second != null) {
                        beamCnt++; beams.addFirst(r.second)
                    }
                }

                '/', '\\' -> {
                    val r = diagonalBeam(p, d, gr[p.row][p.col])
                    p = r.p; d = r.d
                }
            }

            beams[beamCnt] = Beam(p, d)
            beamCnt++
        }

        his.add(vis.size)
        genCnt++
    }

    return vis.size
}

private fun diagonalBeam(pos: Pos, dir: Dir, tile: Char): Beam {
    if (tile == '/') return when (dir) {
        Dir.TOP -> Beam(pos.copy(col = pos.col + 1), Dir.RIGHT)
        Dir.DOWN -> Beam(pos.copy(col = pos.col - 1), Dir.LEFT)
        Dir.LEFT -> Beam(pos.copy(row = pos.row + 1), Dir.DOWN)
        Dir.RIGHT -> Beam(pos.copy(row = pos.row - 1), Dir.TOP)
    }
    if (tile == '\\') return when (dir) {
        Dir.TOP -> Beam(pos.copy(col = pos.col - 1), Dir.LEFT)
        Dir.DOWN -> Beam(pos.copy(col = pos.col + 1), Dir.RIGHT)
        Dir.LEFT -> Beam(pos.copy(row = pos.row - 1), Dir.TOP)
        Dir.RIGHT -> Beam(pos.copy(row = pos.row + 1), Dir.DOWN)
    }
    throw Exception("Invalid state, cannot process a diagonal move")
}

private fun branchBeam(p: Pos, d: Dir, gr: Grid): Pair<Beam, Beam?> {
    if (gr[p.row][p.col] == '|' && (d == Dir.LEFT || d == Dir.RIGHT)) {
        return Pair(
            Beam(p.copy(row = p.row - 1), Dir.TOP),
            Beam(p.copy(row = p.row + 1), Dir.DOWN)
        )
    } else if (gr[p.row][p.col] == '-' && (d == Dir.TOP || d == Dir.DOWN)) {
        return Pair(
            Beam(p.copy(col = p.col - 1), Dir.LEFT),
            Beam(p.copy(col = p.col + 1), Dir.RIGHT)
        )
    }
    return Pair(Beam(moveBeam(p, d), d), null)
}

private fun moveBeam(pos: Pos, dir: Dir): Pos = when (dir) {
    Dir.TOP -> pos.copy(row = pos.row - 1)
    Dir.DOWN -> pos.copy(row = pos.row + 1)
    Dir.LEFT -> pos.copy(col = pos.col - 1)
    Dir.RIGHT -> pos.copy(col = pos.col + 1)
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

private data class Beam(val p: Pos, val d: Dir)

private typealias Grid = List<List<Char>>
