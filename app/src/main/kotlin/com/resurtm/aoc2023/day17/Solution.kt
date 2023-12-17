package com.resurtm.aoc2023.day17

fun launchDay17(testCase: String) {
    val part1 = traverse(readInputGrid(testCase))
    println("Day 17, part 1: $part1")
}

private fun traverse(gr: Grid, beginPosInp: Pos? = null, endPosInp: Pos? = null): Int {
    val beginPos: Pos = beginPosInp ?: Pos()
    val endPos: Pos = endPosInp ?: Pos(gr.size - 1, gr[0].size - 1)

    var min = -1
    var paths = mutableListOf(Path(mutableListOf(beginPos)))

    var genCount = 0
    loop@ while (genCount < 1_000_000) {
        val newPaths = mutableListOf<Path>()
        for (path in paths) {
            val pos = path.items.last()
            val toAdd = mutableListOf<Pos>()

            if (pos.row > 0) {
                val t = pos.copy(row = pos.row - 1)
                if (!path.items.contains(t) && !inLine(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.row < gr.size - 1) {
                val t = pos.copy(row = pos.row + 1)
                if (!path.items.contains(t) && !inLine(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.col > 0) {
                val t = pos.copy(col = pos.col - 1)
                if (!path.items.contains(t) && !inLine(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.col < gr[0].size - 1) {
                val t = pos.copy(col = pos.col + 1)
                if (!path.items.contains(t) && !inLine(path.items + listOf(t)))
                    toAdd.add(t)
            }

            toAdd.forEach {
                val items = (path.items + it).toMutableList()
                val sum = path.sum + gr[it.row][it.col]
                newPaths.add(Path(items, sum))
            }
        }
        paths = newPaths

        paths.forEach {
            if (it.items.last() == endPos && (min == -1 || min > it.sum)) {
                println(it)
                min = it.sum
            }
        }
        if (paths.isEmpty()) break@loop

        genCount++
    }

    return min
}

private fun inLine(lst: List<Pos>): Boolean {
    val lastN = 4
    if (lst.size < lastN + 1) return false
    var vertical = true
    var horizontal = true
    for (item in lst.subList(lst.size - lastN, lst.size))
        if (item.col != lst[lst.size - lastN].col) {
            vertical = false
            break
        }
    for (item in lst.subList(lst.size - lastN, lst.size))
        if (item.row != lst[lst.size - lastN].row) {
            horizontal = false
            break
        }
    return vertical || horizontal
}

private fun printGrid(gr: Grid) {
    println("=====")
    gr.forEach { row ->
        println(row.joinToString(""))
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

private data class Path(val items: MutableList<Pos> = mutableListOf(), val sum: Int = 0)
