package com.resurtm.aoc2023.day23

import kotlin.math.abs

fun launchDay23(testCase: String) {
    println("Day 23, part 1: ${Grid.readInput(testCase).solvePart1()}")
    println("Day 23, part 2: ${Grid.readInput(testCase).solvePart2()}")
}

internal data class Grid(
    val grid: List<List<Node>>,
    val start: Pos,
    val end: Pos
) {
    fun solvePart2(): Int {
        val maxPath = findLongestPath(ignoreDirs = true)
        // this.printGrid(maxPath)
        return maxPath.points.size - 1
    }

    fun solvePart1(): Int = findLongestPath().points.size - 1

    private fun findLongestPath(ignoreDirs: Boolean = false): Path {
        val paths = mutableListOf(
            Path(mutableListOf(start.copy()))
        )
        var maxSize = 0
        var maxPath = paths.first()

        while (paths.size > 0) {
            var pathIdx = 0
            while (pathIdx < paths.size) {
                val path = paths[pathIdx]

//                val nexts = findNextPositionsV1(path.points.last(), path.points, ignoreDirs)
                val nexts = findNextPositionsV3(path.points.last(), path.points)

                if (nexts.isEmpty()) {
                    if (maxSize < path.size && path.points.last() == end) {
                        maxSize = path.size
                        maxPath = path
                    }
                    // println(path)
                    paths.removeAt(pathIdx)
                    continue
                }

                for (nextIdx in nexts.indices) {
                    val newSize = path.size + path.points.last().dist(nexts[nextIdx])
                    val newPoints = path.points.map { it.copy() } + nexts[nextIdx]
                    paths.add(Path(newPoints.toMutableList(), newSize))
                }
                if (nexts.isNotEmpty()) {
                    paths.removeAt(pathIdx)
                    pathIdx--
                }

                pathIdx++
            }
        }

        return maxPath
    }

    internal fun findNextPositionsV3(
        p: Pos,
        exclude: List<Pos> = emptyList(),
    ): List<Pos> {
        val res = mutableListOf<Pos>()
        for (row in p.row + 1..<grid.size) {
            if (grid[row][p.col].ch !in emptyChars) {
                if (Pos(row - 1, p.col) != p)
                    res.add(Pos(row - 1, p.col))
                break
            }
            if (row == grid.size - 1) {
                res.add(Pos(row, p.col))
            }
        }
        for (row in p.row - 1 downTo 0) {
            if (grid[row][p.col].ch !in emptyChars) {
                if (Pos(row + 1, p.col) != p)
                    res.add(Pos(row + 1, p.col))
                break
            }
            if (row == 0) {
                res.add(Pos(row, p.col))
            }
        }
        for (col in p.col + 1..<grid[0].size) {
            if (grid[p.row][col].ch !in emptyChars) {
                if (Pos(p.row, col - 1) != p)
                    res.add(Pos(p.row, col - 1))
                break
            }
            if (col == grid[0].size - 1) {
                res.add(Pos(p.row, col))
            }
        }
        for (col in p.col - 1 downTo 0) {
            if (grid[p.row][col].ch !in emptyChars) {
                if (Pos(p.row, col + 1) != p)
                    res.add(Pos(p.row, col + 1))
                break
            }
            if (col == 0) {
                res.add(Pos(p.row, col))
            }
        }
        return res.filter { !exclude.contains(it) }
    }

    private fun findNextPositionsV1(
        pos: Pos,
        exclude: List<Pos> = emptyList(),
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

internal data class Path(val points: MutableList<Pos> = mutableListOf(), val size: Int = 0)
