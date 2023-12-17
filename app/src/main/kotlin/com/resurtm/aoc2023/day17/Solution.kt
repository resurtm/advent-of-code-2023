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
                nodes[Node(pos, it, Dir.TOP)] = Int.MAX_VALUE
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

    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        if (curr in visited) continue
        visited.add(curr)

        for (nextDir in getNextDirs(curr.dir, curr.steps)) {
            val nextPos = getNextPos(curr, nextDir.first, gr) ?: continue

            val other = Node(nextPos, nextDir.second, nextDir.first)
            if (visited.contains(other)) continue

            val distOther = nodes[other] ?: throw Exception("Invalid state, node cannot be null")
            val distCurr = nodes[curr] ?: throw Exception("Invalid state, node cannot be null")
            nodes[other] = min(distOther, distCurr + gr[nextPos.row][nextPos.col])
            queue.add(other)
        }
    }

    var res = Int.MAX_VALUE
    repeat(4) {
        res = min(res, nodes[Node(end, it, Dir.TOP)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.DOWN)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.LEFT)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, it, Dir.RIGHT)] ?: Int.MAX_VALUE)
    }
    return res
}

private fun getNextPos(node: Node, dir: Dir, gr: Grid): Pos? {
    val pos = when (dir) {
        Dir.TOP -> node.pos.copy(row = node.pos.row - 1)
        Dir.DOWN -> node.pos.copy(row = node.pos.row + 1)
        Dir.LEFT -> node.pos.copy(col = node.pos.col - 1)
        Dir.RIGHT -> node.pos.copy(col = node.pos.col + 1)
    }
    val good = pos.row >= 0 && pos.col >= 0 && pos.row <= gr.size - 1 && pos.col <= gr[0].size - 1
    return if (good) pos else null
}

private fun getNextDirs(dir: Dir, steps: Int): Array<Pair<Dir, Int>> = when (dir) {
    Dir.TOP -> {
        if (steps >= 3) arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        else arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.TOP, steps + 1), Pair(Dir.RIGHT, 1))
    }

    Dir.DOWN -> {
        if (steps >= 3) arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        else arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.DOWN, steps + 1), Pair(Dir.RIGHT, 1))
    }

    Dir.LEFT -> {
        if (steps >= 3) arrayOf(Pair(Dir.TOP, 1), Pair(Dir.DOWN, 1))
        else arrayOf(Pair(Dir.TOP, 1), Pair(Dir.LEFT, steps + 1), Pair(Dir.DOWN, 1))
    }

    Dir.RIGHT -> {
        if (steps >= 3) arrayOf(Pair(Dir.TOP, 1), Pair(Dir.DOWN, 1))
        else arrayOf(Pair(Dir.TOP, 1), Pair(Dir.RIGHT, steps + 1), Pair(Dir.DOWN, 1))
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

private enum class Dir { TOP, DOWN, LEFT, RIGHT }
