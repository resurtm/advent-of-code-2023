package com.resurtm.aoc2023.day17

import java.util.PriorityQueue
import kotlin.math.min

fun launchDay17(testCase: String) {
    val grid = readInputGrid(testCase)
    println("Day 17, part 1: ${traverse(grid)}")
    println("Day 17, part 2: ${traverse(grid, minSize = 4, maxSize = 10)}")
}

private fun traverse(gr: Grid, beginInp: Pos? = null, endInp: Pos? = null, minSize: Int = 0, maxSize: Int = 4): Int {
    val begin: Pos = beginInp ?: Pos()
    val end: Pos = endInp ?: Pos(gr.size - 1, gr[0].size - 1)

    val nodes = mutableMapOf<Node, Int>()
    for (row in gr.indices) {
        for (col in gr[0].indices) {
            val pos = Pos(row, col)
            for (idx in 0..maxSize) {
                nodes[Node(pos, idx, Dir.TOP)] = Int.MAX_VALUE
                nodes[Node(pos, idx, Dir.DOWN)] = Int.MAX_VALUE
                nodes[Node(pos, idx, Dir.LEFT)] = Int.MAX_VALUE
                nodes[Node(pos, idx, Dir.RIGHT)] = Int.MAX_VALUE
            }
        }
    }
    nodes[Node(begin.copy(), 0, Dir.RIGHT)] = 0
    nodes[Node(begin.copy(), 0, Dir.DOWN)] = 0

    val queue = PriorityQueue<ComparableNode>()
    queue.add(ComparableNode(0, Node(begin.copy(), 0, Dir.RIGHT)))
    queue.add(ComparableNode(0, Node(begin.copy(), 0, Dir.DOWN)))

    val visited = mutableSetOf<Node>()

    while (queue.isNotEmpty()) {
        val currRaw = queue.poll() ?: continue
        val curr = currRaw.node
        if (curr in visited) continue
        visited.add(curr)

        for (nextDir in getNextDirs(curr.dir, curr.steps, minSize, maxSize)) {
            val nextPos = getNextPos(curr, nextDir.first, gr) ?: continue

            val other = Node(nextPos, nextDir.second, nextDir.first)
            if (visited.contains(other)) continue

            val distCurr = nodes[curr] ?: throw Exception("Invalid state, node cannot be null")
            val distOther = nodes[other] ?: throw Exception("Invalid state, node cannot be null")

            val newVal = min(distCurr + gr[nextPos.row][nextPos.col], distOther)
            nodes[other] = newVal
            queue.add(ComparableNode(newVal, other))
        }
    }

    var res = Int.MAX_VALUE
    for (idx in minSize..maxSize) {
        res = min(res, nodes[Node(end, idx, Dir.TOP)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, idx, Dir.DOWN)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, idx, Dir.LEFT)] ?: Int.MAX_VALUE)
        res = min(res, nodes[Node(end, idx, Dir.RIGHT)] ?: Int.MAX_VALUE)
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

private fun getNextDirs(dir: Dir, steps: Int, min: Int, max: Int): Array<Pair<Dir, Int>> = when (dir) {
    Dir.TOP -> {
        if (steps >= max) arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        else if (steps < min) arrayOf(Pair(Dir.TOP, steps + 1))
        else arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.TOP, steps + 1), Pair(Dir.RIGHT, 1))
    }

    Dir.DOWN -> {
        if (steps >= max) arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.RIGHT, 1))
        else if (steps < min) arrayOf(Pair(Dir.DOWN, steps + 1))
        else arrayOf(Pair(Dir.LEFT, 1), Pair(Dir.DOWN, steps + 1), Pair(Dir.RIGHT, 1))
    }

    Dir.LEFT -> {
        if (steps >= max) arrayOf(Pair(Dir.TOP, 1), Pair(Dir.DOWN, 1))
        else if (steps < min) arrayOf(Pair(Dir.LEFT, steps + 1))
        else arrayOf(Pair(Dir.TOP, 1), Pair(Dir.LEFT, steps + 1), Pair(Dir.DOWN, 1))
    }

    Dir.RIGHT -> {
        if (steps >= max) arrayOf(Pair(Dir.TOP, 1), Pair(Dir.DOWN, 1))
        else if (steps < min) arrayOf(Pair(Dir.RIGHT, steps + 1))
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

private data class ComparableNode(val priority: Int, val node: Node) : Comparable<ComparableNode> {
    override fun compareTo(other: ComparableNode) =
        if (this.priority > other.priority) 1
        else if (this.priority < other.priority) -1
        else 0
}

private enum class Dir { TOP, DOWN, LEFT, RIGHT }
