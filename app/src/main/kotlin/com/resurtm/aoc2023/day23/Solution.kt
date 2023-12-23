package com.resurtm.aoc2023.day23

fun launchDay23(testCase: String) {
    println("Day 23, part 1: ${Grid.readInput(testCase).solvePart1()}")
    println("Day 23, part 2: ${Grid.readInput(testCase).solvePart2()}")
}

private data class Grid(
    val grid: List<List<Node>>,
    val start: Pos,
    val end: Pos
) {
    fun solvePart1(): Int = findMax(ignoreDirs = false)

    fun solvePart2(): Int = findMax(ignoreDirs = true)

    private fun findMax(ignoreDirs: Boolean): Int {
        val paths = mutableListOf(mutableListOf(start.copy()))
        var maxSize = 0

        while (paths.size > 0) {
            var pathIdx = 0
            while (pathIdx < paths.size) {
                val nexts = findNextPositions(paths[pathIdx], ignoreDirs = ignoreDirs)
                if (nexts.isEmpty()) {
                    if (maxSize < paths[pathIdx].size)
                        maxSize = paths[pathIdx].size
                    paths.removeAt(pathIdx)
                    continue
                }

                for (nextIdx in nexts.indices) {
                    if (nextIdx == 0) {
                        paths[pathIdx].add(nexts[0])
                    } else {
                        val copied = paths[pathIdx].map { it.copy() }
                        copied.removeLast()
                        paths.add((copied + nexts[nextIdx]).toMutableList())
                    }
                }

                pathIdx++
            }
        }

        return maxSize - 1
    }

    private fun findNextPositions(
        visited: List<Pos>,
        posInp: Pos? = null,
        ignoreDirs: Boolean = false,
    ): List<Pos> {
        val pos = posInp ?: visited.last()
        val freeChs = arrayOf('.', '^', '>', 'v', '<')
        val directions = arrayOf(
            Pair(Pos(-1L, 0L), 'v'),
            Pair(Pos(1L, 0L), '^'),
            Pair(Pos(0L, -1L), '>'),
            Pair(Pos(0L, 1L), '<'),
        )
        return directions
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
                val ch = grid[it.first.row.toInt()][it.first.col.toInt()].ch
                if (ignoreDirs)
                    ch in freeChs
                else
                    ch != it.second && ch in freeChs
            }
            .map { it.first }
            .filter { visited.indexOf(it) == -1 }
            .toList()
    }

    fun print() {
        println("----------")
        for (row in grid.indices) {
            for (col in grid[row].indices)
                print(grid[row][col].ch)
            println()
        }
    }

    companion object {
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
            val start = Pos(row = 0L, col = grid[0].indexOfFirst { it.ch == '.' }.toLong())
            val end = Pos(row = lastRow.toLong(), col = grid[lastRow].indexOfFirst { it.ch == '.' }.toLong())
            return Grid(grid, start, end)
        }
    }
}

private data class Node(val ch: Char)

private data class Pos(val row: Long, val col: Long)
