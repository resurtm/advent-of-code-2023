package com.resurtm.aoc2023.day17

import kotlin.math.min

fun launchDay17(testCase: String) {
    val part1 = traverse(readInputGrid(testCase))
    println("Day 17, part 1: $part1")
}

private fun traverse(gr: Grid, beginInp: Pos? = null, endInp: Pos? = null): Int {
    val begin: Pos = beginInp ?: Pos()
    val end: Pos = endInp ?: Pos(gr.size - 1, gr[0].size - 1)

    val nodes = mutableMapOf<Node, Int>()
    for (row in gr.indices) {
        for (col in gr[0].indices) {
            val pos = Pos(row, col)
            repeat(4) {
                nodes[Node(pos, it, Dir.UP)] = Int.MAX_VALUE
                nodes[Node(pos, it, Dir.DOWN)] = Int.MAX_VALUE
                nodes[Node(pos, it, Dir.LEFT)] = Int.MAX_VALUE
                nodes[Node(pos, it, Dir.RIGHT)] = Int.MAX_VALUE
            }
        }
    }
    nodes[Node(begin.copy(), 0, Dir.RIGHT)] = 0

    val queue = ArrayDeque<Node>()
    queue.add(Node(begin.copy(), 0, Dir.RIGHT))

    val visited = mutableSetOf<Node>()

    while (true) {
        val node = queue.removeFirstOrNull() ?: break
        if (node in visited) continue
        visited.add(node)

        for (nextDir in getNextDirs(node.dir, node.steps)) {
            val nextPos = getNextPos(node, nextDir.first, gr)
                ?: continue
            val other = Node(nextPos, nextDir.second, nextDir.first)
            val dist1 = nodes[other]
                ?: throw Exception("Invalid state, node cannot be null, $nextPos, $nextDir")
            val dist2 = nodes[node]
                ?: throw Exception("Invalid state, node cannot be null, $nextPos, $nextDir")
            val dist = min(dist1, dist2 + gr[node.pos.row][node.pos.col])
            nodes[other] = dist
            queue.add(other)
        }
    }

    var res = Int.MAX_VALUE
    repeat(4) {
        res = min(res, nodes[Node(end, it, Dir.UP)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.DOWN)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.LEFT)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.RIGHT)] ?: Int.MAX_VALUE)
    }
    return res - gr[begin.row][begin.col] + gr[end.row][end.col]
}

private fun getNextPos(node: Node, dir: Dir, gr: Grid): Pos? {
    val pos = when (dir) {
        Dir.UP -> node.pos.copy(row = node.pos.row - 1)
        Dir.DOWN -> node.pos.copy(row = node.pos.row + 1)
        Dir.LEFT -> node.pos.copy(col = node.pos.col - 1)
        Dir.RIGHT -> node.pos.copy(col = node.pos.col + 1)
    }
    val good = pos.row >= 0 && pos.col >= 0 && pos.row <= gr.size - 1 && pos.col <= gr[0].size - 1
    return if (good) pos else null
}

private fun getNextDirs(dir: Dir, steps: Int): List<Pair<Dir, Int>> = when (dir) {
    Dir.UP -> {
        val base = listOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        if (steps >= 3) base else base + listOf(Pair(Dir.UP, steps + 1))
    }

    Dir.DOWN -> {
        val base = listOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        if (steps >= 3) base else base + listOf(Pair(Dir.DOWN, steps + 1))
    }

    Dir.LEFT -> {
        val base = listOf(Pair(Dir.UP, 1), Pair(Dir.DOWN, 1))
        if (steps >= 3) base else base + listOf(Pair(Dir.LEFT, steps + 1))
    }

    Dir.RIGHT -> {
        val base = listOf(Pair(Dir.UP, 1), Pair(Dir.DOWN, 1))
        if (steps >= 3) base else base + listOf(Pair(Dir.RIGHT, steps + 1))
    }
}

private fun readInputGrid(testCase: String): Grid {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read an input")
    val input = mutableListOf<List<Int>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        input.add(rawLine.trim().map { "$it".toInt() })
    }
    return input
}

private typealias Grid = List<List<Int>>

private data class Pos(val row: Int = 0, val col: Int = 0)

private data class Node(val pos: Pos, val steps: Int, val dir: Dir)

private enum class Dir { UP, DOWN, LEFT, RIGHT }
