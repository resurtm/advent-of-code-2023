package com.resurtm.aoc2023.day23

import kotlin.math.abs
import kotlin.math.max

fun launchDay23(testCase: String) {
    println("Day 23, part 1: ${Grid.readInput(testCase).solvePart1()}")
    println("Day 23, part 2 (slow): ${Grid.readInput(testCase).solvePart2Slow()}")
    println("Day 23, part 2 (fast): ${Grid.readInput(testCase).solvePart2Fast()}")
    println("Day 23, part 2 (graph): ${Grid.readInput(testCase).solvePart2Graph()}")
}

internal data class Grid(
    val grid: List<List<Node>>,
    val start: Pos,
    val end: Pos
) {
    private val graph = mutableMapOf<Pos, MutableSet<Pos>>()
    private val visited = mutableSetOf<Pos>()
    private val topological = mutableListOf<Pos>()
    private val dists = mutableMapOf<Pos, Int>()

    fun solvePart1(): Int = findLongestPath().first

    fun solvePart2Slow(): Int = findLongestPath(ignoreDirs = true).first

    fun solvePart2Fast(): Int = findLongestPath(useIntersections = true).first

    fun solvePart2Graph(): Int {
        buildGraph()
//         println(graph)
        graph.forEach { println(it) }
        buildTopological()
        // println(topological)
        val longestPath = calcLongestPath()
        buildLongestPath()

        return longestPath
    }

    private fun buildLongestPath() {
        var curr = start
        val path = mutableListOf<Pos>()

        while (curr != end) {
            path.add(curr)

            val nexts = graph[curr] ?: break
            if (nexts.isEmpty()) {
                break
            }

            var maxPos = nexts.first()
            var maxDst = dists[maxPos] ?: break

            nexts.forEach {
                val dst = dists[it]
                if (dst != null && maxDst > dst) {
                    maxPos = it
                    maxDst = dst
                }
            }

            curr = maxPos
        }
        // println(dists)
        // path.add(end)

        printGrid(path)
        // println(path)
    }

    private fun calcLongestPath(): Int {
        dists.clear()
        topological.forEach { dists[it] = Int.MIN_VALUE }
        dists[start] = 0

        topological.forEach { node ->
            val nexts = graph[node] ?: emptySet()
            nexts.forEach { next ->
                dists[next] = max(
                    dists[next] ?: Int.MIN_VALUE,
                    (dists[node] ?: Int.MIN_VALUE) + node.dist(next)
                )
            }
        }

        return dists.values.fold(Int.MIN_VALUE) { acc: Int, i: Int -> if (acc < i) i else acc }
    }

    private fun buildTopological() {
        visited.clear()
        topological.clear()
        graph.entries.forEach { (node, nexts) ->
            buildTopologicalVisit(node, nexts)
        }
        topological.reverse()
    }

    private fun buildTopologicalVisit(node: Pos, nexts: Set<Pos>) {
        if (node in visited)
            return
        visited.add(node)
        nexts.forEach { next ->
            buildTopologicalVisit(next, graph[next] ?: emptySet())
        }
        topological.add(node)
    }

    private fun buildGraph() {
        graph.clear()

        val queue = ArrayDeque<Pos>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            println(pos)

            val nexts = findNextPositionsV2(pos)
            nexts.forEach {
                // check for existing direction
                val ex = graph[it] ?: mutableSetOf()
                if (ex.isEmpty() || !ex.contains(pos)) {
                    // add item to directed graph
                    val st = graph[pos] ?: mutableSetOf()
                    st.add(it)
                    graph[pos] = st

                    queue.add(it)
                }
            }
        }

        // buildGraphRecursive(start)
    }

    private fun buildGraphRecursive(pos: Pos, history: List<Pos> = emptyList()) {
        val nexts = findNextPositionsV2(pos, history)
        nexts.forEach {
            // check for existing direction
            val ex = graph[it] ?: mutableSetOf()
            if (ex.isEmpty() || !ex.contains(pos)) {
                // add item to directed graph
                val st = graph[pos] ?: mutableSetOf()
                st.add(it)
                graph[pos] = st

                buildGraphRecursive(it, history + pos)
            }
        }
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

        printGrid(maxPath.points)
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
