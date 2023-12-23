package com.resurtm.aoc2023.day23

fun launchDay23(testCase: String) {
    println("Day 23, part 1: ${Grid.readInput(testCase).solvePart1()}")
    println("Day 23, part 2: ${Grid.readInput(testCase).solvePart2Legacy()}")
    // println("Day 23, part 2: ${Grid.readInput(testCase).solvePart2Graph()}")
}

private data class Grid(
    val grid: List<List<Node>>,
    val start: Pos,
    val end: Pos
) {
    fun solvePart2Graph(): Int {
        val queue = ArrayDeque<Pos>()
        queue.add(start)
        grid[start.row.toInt()][start.col.toInt()].dist = 0L

        while (queue.isNotEmpty()) {
            val currPos = queue.removeFirstOrNull() ?: break
            val currNode = grid[currPos.row.toInt()][currPos.col.toInt()]
            currNode.visited = true

            val nexts = findNextPositionsV2(currPos)
            if (currNode.dist != Long.MIN_VALUE) {
                nexts.forEach {
                    val otherNode = grid[it.row.toInt()][it.col.toInt()]
                    if (otherNode.dist < currNode.dist + 1)
                        otherNode.dist = currNode.dist + 1
                }
            }
            queue.addAll(nexts)
        }

        var maxDist = Long.MIN_VALUE
        grid.forEach { row ->
            row.forEach {
                if (maxDist < it.dist)
                    maxDist = it.dist
            }
        }
        return maxDist.toInt()
    }

    fun solvePart2Legacy(): Int {
        val maxPath = findMax(ignoreDirs = true)
        // this.printGrid(maxPath)
        return maxPath.size - 1
    }

    fun solvePart1(): Int = findMax().size - 1

    private fun findMax(ignoreDirs: Boolean = false): List<Pos> {
        val paths = mutableListOf(mutableListOf(start.copy()))
        var maxSize = 0
        var maxPath = paths.first()

        while (paths.size > 0) {
            var pathIdx = 0
            while (pathIdx < paths.size) {
                val nexts = findNextPositions(paths[pathIdx].last(), paths[pathIdx], ignoreDirs)
                if (nexts.isEmpty()) {
                    if (maxSize < paths[pathIdx].size && paths[pathIdx].last() == end) {
                        maxSize = paths[pathIdx].size
                        maxPath = paths[pathIdx]
                    }
                    paths.removeAt(pathIdx)
                    continue
                }

                for (nextIdx in nexts.indices) {
                    val copied = paths[pathIdx].map { it.copy() } + nexts[nextIdx]
                    paths.add(copied.toMutableList())
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

    private fun findNextPositionsV2(pos: Pos): List<Pos> = directions
        .asSequence()
        .map { it.first }
        .map { it.copy(row = pos.row + it.row, col = pos.col + it.col) }
        .filter { it.row >= 0L && it.col >= 0L && it.row <= grid.size - 1L && it.col <= grid[0].size - 1L }
        .filter {
            val node = grid[it.row.toInt()][it.col.toInt()]
            node.ch in freeChs && !node.visited
        }
        .toList()

    private fun findNextPositions(pos: Pos, visited: List<Pos>, ignoreDirs: Boolean): List<Pos> = directions
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
            if (ignoreDirs) ch in freeChs else ch != it.second && ch in freeChs
        }
        .map { it.first }
        .filter { visited.indexOf(it) == -1 }
        .toList()

    fun printGrid(maxPath: List<Pos> = listOf()) {
        println("----------")
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val pos = Pos(row.toLong(), col.toLong())
                print(if (maxPath.contains(pos)) 'O' else grid[row][col].ch)
            }
            println()
        }
    }

    companion object {
        private val freeChs = arrayOf('.', '^', '>', 'v', '<')

        private val directions = arrayOf(
            Pair(Pos(-1L, 0L), 'v'),
            Pair(Pos(1L, 0L), '^'),
            Pair(Pos(0L, -1L), '>'),
            Pair(Pos(0L, 1L), '<'),
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
            val start = Pos(row = 0L, col = grid[0].indexOfFirst { it.ch == '.' }.toLong())
            val end = Pos(row = lastRow.toLong(), col = grid[lastRow].indexOfFirst { it.ch == '.' }.toLong())
            return Grid(grid, start, end)
        }
    }
}

private data class Node(val ch: Char, var dist: Long = Long.MIN_VALUE, var visited: Boolean = false)

private data class Pos(val row: Long, val col: Long)
