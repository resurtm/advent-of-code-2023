package com.resurtm.aoc2023.day17

fun launchDay17(testCase: String) {
    // val part1 = traverse(readInputGrid(testCase))
    val part1 = traverse2(readInputGrid(testCase))
    println("Day 17, part 1: $part1")
}

private fun traverse2(gr: Grid, beginPosInp: Pos? = null, endPosInp: Pos? = null): Int {
    var path = buildPath(gr, beginPosInp, endPosInp)

    printGrid(gr)
    printGrid(gr, path.items)

    return 0
}

private fun buildPath(gr: Grid, beginPosInp: Pos? = null, endPosInp: Pos? = null): Path {
    val beginPos: Pos = beginPosInp ?: Pos()
    val endPos: Pos = endPosInp ?: Pos(gr.size - 1, gr[0].size - 1)

    var pos = beginPos.copy()
    val path = Path(mutableListOf(pos.copy()))
    var toggle = true

    while (pos != endPos) {
        val ts = mutableListOf<Pos>()

        if (toggle) {
            val aa = pos.copy(row = pos.row + 1)
            if (pos.row < gr.size - 1 && !path.items.contains(aa) && !inl(path.items + listOf(aa))) ts.add(aa)
            val bb = pos.copy(col = pos.col + 1)
            if (pos.col < gr[0].size - 1 && !path.items.contains(bb) && !inl(path.items + listOf(bb))) ts.add(bb)
            val cc = pos.copy(row = pos.row - 1)
            if (pos.row > 0 && !path.items.contains(cc) && !inl(path.items + listOf(cc))) ts.add(cc)
            val dd = pos.copy(col = pos.col - 1)
            if (pos.col > 0 && !path.items.contains(dd) && !inl(path.items + listOf(dd))) ts.add(dd)
        } else {
            val xx = pos.copy(col = pos.col + 1)
            if (pos.col < gr[0].size - 1 && !path.items.contains(xx) && !inl(path.items + listOf(xx))) ts.add(xx)
            val yy = pos.copy(row = pos.row + 1)
            if (pos.row < gr.size - 1 && !path.items.contains(yy) && !inl(path.items + listOf(yy))) ts.add(yy)
            val zz = pos.copy(col = pos.col - 1)
            if (pos.col > 0 && !path.items.contains(zz) && !inl(path.items + listOf(zz))) ts.add(zz)
            val ww = pos.copy(row = pos.row - 1)
            if (pos.row > 0 && !path.items.contains(ww) && !inl(path.items + listOf(ww))) ts.add(ww)
        }

        pos = ts.first()
        path.items.add(pos)
        path.sum += gr[pos.row][pos.col]
        toggle = !toggle
    }

    return path
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
                if (!path.items.contains(t) && !inl(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.row < gr.size - 1) {
                val t = pos.copy(row = pos.row + 1)
                if (!path.items.contains(t) && !inl(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.col > 0) {
                val t = pos.copy(col = pos.col - 1)
                if (!path.items.contains(t) && !inl(path.items + listOf(t)))
                    toAdd.add(t)
            }
            if (pos.col < gr[0].size - 1) {
                val t = pos.copy(col = pos.col + 1)
                if (!path.items.contains(t) && !inl(path.items + listOf(t)))
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

private fun inl(lst: List<Pos>): Boolean {
    val lastN = 5
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

private fun printGrid(gr: Grid, pos: List<Pos> = emptyList()) {
    println("=====")
    gr.forEachIndexed { row, rowItems ->
        rowItems.forEachIndexed { col, item ->
            val p = Pos(row, col)
            print(if (pos.contains(p)) 'X' else item)
        }
        println()
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

private data class Path(val items: MutableList<Pos> = mutableListOf(), var sum: Int = 0)
