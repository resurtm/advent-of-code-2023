package com.resurtm.aoc2023.day17

fun launchDay17(testCase: String) {
    traverse(Pos(), readInputGrid(testCase))
}

private fun traverse(startPos: Pos, gr: Grid) {
    var paths = mutableListOf(Path(mutableListOf(startPos)))

    var genCount = 0
    while (genCount < 4) {
        val newPaths = mutableListOf<Path>()
        for (path in paths) {
            val pos = path.items.last()
            val toAdd = mutableListOf<Pos>()

            if (pos.row > 0) {
                val tmpPos = pos.copy(row = pos.row - 1)
                if (!path.items.contains(tmpPos) && !inLine(path.items + listOf(tmpPos)))
                    toAdd.add(tmpPos)
            }
            if (pos.row < gr.size - 2) {
                val tmpPos = pos.copy(row = pos.row + 1)
                if (!path.items.contains(tmpPos) && !inLine(path.items + listOf(tmpPos)))
                    toAdd.add(tmpPos)
            }
            if (pos.col > 0) {
                val tmpPos = pos.copy(col = pos.col - 1)
                if (!path.items.contains(tmpPos) && !inLine(path.items + listOf(tmpPos)))
                    toAdd.add(tmpPos)
            }
            if (pos.col < gr[0].size - 2) {
                val tmpPos = pos.copy(col = pos.col + 1)
                if (!path.items.contains(tmpPos) && !inLine(path.items + listOf(tmpPos)))
                    toAdd.add(tmpPos)
            }

            toAdd.forEach {
                val items = (path.items + it).toMutableList()
                val sum = path.sum + gr[it.row][it.col]
                newPaths.add(Path(items, sum))
            }
        }
        paths = newPaths

        genCount++
    }
}

private fun inLine(lst: List<Pos>): Boolean {
    if (lst.size < 4) return false
    var vertical = true
    var horizontal = true
    for (item in lst.subList(lst.size - 3, lst.size))
        if (item.col != lst[lst.size - 3].col) {
            vertical = false
            break
        }
    for (item in lst.subList(lst.size - 3, lst.size))
        if (item.row != lst[lst.size - 3].row) {
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
