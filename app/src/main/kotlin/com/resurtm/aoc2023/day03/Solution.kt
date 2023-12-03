package com.resurtm.aoc2023.day03

fun launchDay03(testCase: String) {
    val grid = buildGrid(testCase)
    val digits = '0'..'9'
    val digitsEx = digits + '.'

    fun checkNeighbors(rowIndex: Int, chIndex: Int): Boolean {
        val w = grid[0].size - 1
        val h = grid.size - 1

        val tl = if (rowIndex == 0 || chIndex == 0) '.' else grid[rowIndex - 1][chIndex - 1]
        val tm = if (rowIndex == 0) '.' else grid[rowIndex - 1][chIndex]
        val tr = if (rowIndex == 0 || chIndex == w) '.' else grid[rowIndex - 1][chIndex + 1]
        val bl = if (rowIndex == h || chIndex == 0) '.' else grid[rowIndex + 1][chIndex - 1]
        val bm = if (rowIndex == h) '.' else grid[rowIndex + 1][chIndex]
        val br = if (rowIndex == h || chIndex == w) '.' else grid[rowIndex + 1][chIndex + 1]
        val ml = if (chIndex == 0) '.' else grid[rowIndex][chIndex - 1]
        val mr = if (chIndex == w) '.' else grid[rowIndex][chIndex + 1]

        return !(tl in digitsEx && tm in digitsEx && tr in digitsEx && bl in digitsEx && bm in digitsEx && br in digitsEx && ml in digitsEx && mr in digitsEx)
    }

    var active = false
    var adj = false
    var accum = ""
    var result = 0

    for ((rowIndex, row) in grid.withIndex()) {
        for ((chIndex, ch) in row.withIndex()) {
            if (!active && ch in digits) {
                active = true
                adj = false
                accum = ""
            }
            if (active && ch !in digits) {
                if (adj) {
                    result += accum.toInt()
                }
                active = false
                adj = false
                accum = ""
            }
            if (active) {
                adj = checkNeighbors(rowIndex, chIndex) || adj
                accum += ch
            }
        }

        if (adj) {
            result += accum.toInt()
        }
        active = false
        adj = false
        accum = ""
    }

    println("Part 1 answer: $result")
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
