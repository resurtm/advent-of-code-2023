package com.resurtm.aoc2023.day03

typealias GearPos = Pair<Int, Int>

fun launchDay03(testCase: String) {
    val grid = buildGrid(testCase)
    val gears = mutableMapOf<GearPos, MutableList<Int>>()

    val digits = '0'..'9'
    val digitsEx = digits + '.'

    fun checkNeighbors(rowIndex: Int, chIndex: Int): Pair<Boolean, GearPos?> {
        val w = grid[0].size - 1
        val h = grid.size - 1
        var gear: GearPos? = null

        val tl = if (rowIndex == 0 || chIndex == 0) '.' else grid[rowIndex - 1][chIndex - 1]
        if (tl == '*') gear = Pair(rowIndex - 1, chIndex - 1)

        val tm = if (rowIndex == 0) '.' else grid[rowIndex - 1][chIndex]
        if (tm == '*') gear = Pair(rowIndex - 1, chIndex)

        val tr = if (rowIndex == 0 || chIndex == w) '.' else grid[rowIndex - 1][chIndex + 1]
        if (tr == '*') gear = Pair(rowIndex - 1, chIndex + 1)

        val bl = if (rowIndex == h || chIndex == 0) '.' else grid[rowIndex + 1][chIndex - 1]
        if (bl == '*') gear = Pair(rowIndex + 1, chIndex - 1)

        val bm = if (rowIndex == h) '.' else grid[rowIndex + 1][chIndex]
        if (bm == '*') gear = Pair(rowIndex + 1, chIndex)

        val br = if (rowIndex == h || chIndex == w) '.' else grid[rowIndex + 1][chIndex + 1]
        if (br == '*') gear = Pair(rowIndex + 1, chIndex + 1)

        val ml = if (chIndex == 0) '.' else grid[rowIndex][chIndex - 1]
        if (ml == '*') gear = Pair(rowIndex, chIndex - 1)

        val mr = if (chIndex == w) '.' else grid[rowIndex][chIndex + 1]
        if (mr == '*') gear = Pair(rowIndex, chIndex + 1)

        val res1 =
            !(tl in digitsEx && tm in digitsEx && tr in digitsEx && bl in digitsEx && bm in digitsEx && br in digitsEx && ml in digitsEx && mr in digitsEx)

        return Pair(res1, gear)
    }

    var active = false
    var adj = false
    var activeGear: GearPos? = null
    var accum = ""
    var result = 0

    for ((rowIndex, row) in grid.withIndex()) {
        for ((chIndex, ch) in row.withIndex()) {
            if (!active && ch in digits) {
                active = true
                adj = false
                activeGear = null
                accum = ""
            }
            if (active && ch !in digits) {
                if (adj) {
                    result += accum.toInt()
                    if (activeGear != null) {
                        if (gears.containsKey(activeGear)) {
                            gears[activeGear]?.add(accum.toInt())
                        } else {
                            gears[activeGear] = mutableListOf(accum.toInt())
                        }
                    }
                }
                active = false
                adj = false
                activeGear = null
                accum = ""
            }
            if (active) {
                val chRes = checkNeighbors(rowIndex, chIndex)
                adj = chRes.first || adj
                if (chRes.second != null) {
                    activeGear = chRes.second!!
                }
                accum += ch
            }
        }

        if (adj) {
            result += accum.toInt()
            if (activeGear != null) {
                if (gears.containsKey(activeGear)) {
                    gears[activeGear]?.add(accum.toInt())
                } else {
                    gears[activeGear] = mutableListOf(accum.toInt())
                }
            }
        }
        active = false
        adj = false
        activeGear = null
        accum = ""
    }

    var gearsResult = 0
    for (items in gears.values) {
        if (items.size == 2) {
            gearsResult += items[0] * items[1]
        }
    }

    println("Part 1 answer: $result")
    println("Part 2 answer: $gearsResult")
}

private fun buildGrid(testCase: String): ArrayList<ArrayList<Char>> {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    val result = ArrayList<ArrayList<Char>>()
    while (true) {
        val line = reader.readLine() ?: break
        val row = ArrayList<Char>()
        for (ch in line) {
            row.add(ch)
        }
        result.add(row)
    }
    return result
}
