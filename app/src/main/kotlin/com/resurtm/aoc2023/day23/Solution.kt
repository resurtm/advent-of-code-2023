package com.resurtm.aoc2023.day23

import kotlin.math.abs
import kotlin.math.max

fun launchDay23(testCase: String) {
    println("Day 23, part 1: ${Grid.readInput(testCase).solvePart1()}")
    // println("Day 23, part 2 (slow): ${Grid.readInput(testCase).solvePart2Slow()}")
    // println("Day 23, part 2 (fast): ${Grid.readInput(testCase).solvePart2Fast()}")
    println("Day 23, part 2 (best): ${Grid.readInput(testCase).solvePart2Best()}")
}

internal data class Grid(
    val grid: List<List<Node>>,
    val start: Pos,
    val end: Pos
) {
    fun solvePart1(): Int = findLongestPath().first

    fun solvePart2Slow(): Int = findLongestPath(ignoreDirs = true).first

    fun solvePart2Fast(): Int = findLongestPath(useIntersections = true).first

    fun solvePart2Best(): Int {
        val emptyPositions = mutableSetOf<Pos>()
        grid.forEachIndexed { row, items ->
            items.forEachIndexed { col, item ->
                if (item.ch in emptyChars)
                    emptyPositions.add(Pos(row, col))
            }
        }

        val nonPacked = mutableMapOf<Pos, MutableList<Pos>>()
        emptyPositions.forEach { pos ->
            directions.forEach { dir ->
                val lst = nonPacked[pos]
                val add = Pos(pos.row + dir.first.row, pos.col + dir.first.col)
                if (add.row >= 1 && add.col >= 1 && add.row <= grid.size - 2 && add.col <= grid[0].size - 2) {
                    if (lst == null) nonPacked[pos] = mutableListOf(add)
                    else lst.add(add)
                }
            }
        }

        val packed = mutableMapOf<Pos, List<Pair<Pos, Int>>>()
        emptyPositions.forEach { pos0 ->
            packed[pos0] = findNextPositionsV2(pos0).map { pos1 ->
                Pair(pos1, pos1.dist(pos0))
            }
        }
        // packed.forEach { println(it) }

        return search(packed, start, end, 0, mutableSetOf())
    }

    private fun search(
        packed: Map<Pos, List<Pair<Pos, Int>>>,
        pos: Pos,
        stop: Pos,
        dist: Int,
        history: MutableSet<Pos>,
    ): Int {
        if (pos == stop) return dist

        history.add(pos)
        val best = (packed[pos] ?: emptyList()).map { v ->
            if (v.first !in history) {
                search(packed, v.first, stop, v.second + dist, history)
            } else {
                0
            }
        }.max()
        val bestRes = max(0, best)
        // println(bestRes)
        history.remove(pos)

        return bestRes
    }

    private fun findLongestPath(
        ignoreDirs: Boolean = false,
        useIntersections: Boolean = false,
    ): Pair<Int, Path> {
        val paths = mutableListOf(
            Path(mutableListOf(start.copy())),
        )
        var maxSize = 0
        var maxPath = paths.first()

        while (paths.size > 0) {
            var pathIdx = 0
            while (pathIdx < paths.size) {
                val path = paths[pathIdx]

                val nexts = if (useIntersections) findNextPositionsV2(path.points.last(), path.points)
                else findNextPositionsV1(path.points.last(), path.points, ignoreDirs)

                if (nexts.isEmpty()) {
                    if (path.points.last() == end && path.size() > maxSize) {
                        maxSize = path.size()
                        maxPath = path
                        // println(maxSize)
                    }
                    paths.removeAt(pathIdx)
                    continue
                }

                for (nextIdx in nexts.indices) {
                    val newPoints = path.points.map { it.copy() } + nexts[nextIdx]
                    paths.add(Path(newPoints.toMutableList()))
                }
                if (nexts.isNotEmpty()) {
                    paths.removeAt(pathIdx)
                    pathIdx--
                }

                pathIdx++
            }
        }

        // printGrid(maxPath.points)
        return Pair(maxSize, maxPath)
    }

    internal fun findNextPositionsV2(
        p: Pos,
        exclude: Collection<Pos> = emptyList(),
    ): List<Pos> {
        val res = mutableListOf<Pos>()

        // down dir
        for (row in p.row + 1..<grid.size) {
            if (
                grid[row][p.col].ch in emptyChars &&
                (grid[row][p.col - 1].ch in emptyChars || grid[row][p.col + 1].ch in emptyChars)
            ) {
                res.add(Pos(row, p.col))
                break
            }
            if (grid[row][p.col].ch !in emptyChars) {
                if (Pos(row - 1, p.col) != p)
                    res.add(Pos(row - 1, p.col))
                break
            }
            if (row == grid.size - 1) {
                res.add(Pos(row, p.col))
            }
        }

        // up dir
        for (row in p.row - 1 downTo 0) {
            if (
                grid[row][p.col].ch in emptyChars &&
                (grid[row][p.col - 1].ch in emptyChars || grid[row][p.col + 1].ch in emptyChars)
            ) {
                res.add(Pos(row, p.col))
                break
            }
            if (grid[row][p.col].ch !in emptyChars) {
                if (Pos(row + 1, p.col) != p)
                    res.add(Pos(row + 1, p.col))
                break
            }
            if (row == 0) {
                res.add(Pos(row, p.col))
            }
        }

        // right dir
        for (col in p.col + 1..<grid[0].size) {
            if (
                grid[p.row][col].ch in emptyChars &&
                (grid[p.row - 1][col].ch in emptyChars || grid[p.row + 1][col].ch in emptyChars)
            ) {
                res.add(Pos(p.row, col))
                break
            }
            if (grid[p.row][col].ch !in emptyChars) {
                if (Pos(p.row, col - 1) != p)
                    res.add(Pos(p.row, col - 1))
                break
            }
            if (col == grid[0].size - 1) {
                res.add(Pos(p.row, col))
            }
        }

        // left dir
        for (col in p.col - 1 downTo 0) {
            if (
                grid[p.row][col].ch in emptyChars &&
                (grid[p.row - 1][col].ch in emptyChars || grid[p.row + 1][col].ch in emptyChars)
            ) {
                res.add(Pos(p.row, col))
                break
            }
            if (grid[p.row][col].ch !in emptyChars) {
                if (Pos(p.row, col + 1) != p)
                    res.add(Pos(p.row, col + 1))
                break
            }
            if (col == 0) {
                res.add(Pos(p.row, col))
            }
        }

        return res.filter { !exclude.contains(it) }.toSet().toList()
    }

    private fun findNextPositionsV1(
        pos: Pos,
        exclude: Collection<Pos> = emptySet(),
        ignoreDirs: Boolean = false,
    ): List<Pos> = directions
        .asSequence()
        .map {
            val p = it.first
            Pair(
                it.first.copy(row = pos.row + p.row, col = pos.col + p.col),
                it.second
            )
        }
        .filter {
            val p = it.first
            p.row >= 0L && p.col >= 0L && p.row <= grid.size - 1L && p.col <= grid[0].size - 1L
        }
        .filter {
            val ch = grid[it.first.row][it.first.col].ch
            if (ignoreDirs) ch in emptyChars else ch != it.second && ch in emptyChars
        }
        .filter { it.first !in exclude }
        .map { it.first }
        .toList()

    fun printGrid(maxPath: List<Pos> = listOf()) {
        println("----------")
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                print(if (maxPath.contains(Pos(row, col))) 'O' else grid[row][col].ch)
            }
            println()
        }
    }

    companion object {
        private val emptyChars = arrayOf('.', '^', '>', 'v', '<')

        private val directions = arrayOf(
            Pair(Pos(-1, 0), 'v'),
            Pair(Pos(1, 0), '^'),
            Pair(Pos(0, -1), '>'),
            Pair(Pos(0, 1), '<'),
        )

        internal fun readInput(testCase: String): Grid {
            val reader =
                object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
                    ?: throw Exception("Cannot read an input, probably it is invalid")

            val grid = mutableListOf<List<Node>>()
            while (true) {
                val rawLine = reader.readLine()
                    ?: break
                if (rawLine.trim().isEmpty())
                    continue
                val row = rawLine.trim().toList().map { Node(it) }
                grid.add(row)
            }

            val lastRow = grid.size - 1
            val start = Pos(row = 0, col = grid[0].indexOfFirst { it.ch == '.' })
            val end = Pos(row = lastRow, col = grid[lastRow].indexOfFirst { it.ch == '.' })
            return Grid(grid, start, end)
        }
    }
}

internal data class Node(val ch: Char)

internal data class Pos(val row: Int, val col: Int) {
    internal fun dist(some: Pos) = abs(some.row - this.row) + abs(some.col - this.col)
}

internal data class Path(val points: MutableList<Pos> = mutableListOf()) {
    internal fun size(): Int {
        var res = 0
        for (idx in 0..points.size - 2) {
            res += points[idx].dist(points[idx + 1])
        }
        return res
    }
}
