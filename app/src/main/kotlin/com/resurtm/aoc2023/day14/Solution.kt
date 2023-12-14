package com.resurtm.aoc2023.day14

fun launchDay14(testCase: String) {
    val input = readInput(testCase)
    repeat(3) {
        tiltNorth(input)
        tiltWest(input)
        tiltSouth(input)
        tiltEast(input)
    }
    println(calcSum(input))
    input.forEach { println(it.joinToString("")) }
}

private fun tiltNorth(grid: Input) {
    for (row in grid.indices) {
        for (col in grid[row].indices) {
            if (grid[row][col] == 'O') for (row2 in row - 1 downTo -1) {
                if (row2 == -1 || grid[row2][col] != '.') {
                    val x = grid[row][col]
                    grid[row][col] = grid[row2 + 1][col]
                    grid[row2 + 1][col] = x
                    break
                }
            }
        }
    }
}

private fun tiltWest(grid: Input) {
    for (col in grid[0].indices) {
        for (row in grid.indices) {
            if (grid[row][col] == 'O') for (col2 in col - 1 downTo -1) {
                if (col2 == -1 || grid[row][col2] != '.') {
                    val x = grid[row][col]
                    grid[row][col] = grid[row][col2 + 1]
                    grid[row][col2 + 1] = x
                    break
                }
            }
        }
    }
}

private fun tiltSouth(grid: Input) {
    for (row in grid.size - 1 downTo 0) {
        for (col in grid[row].indices) {
            if (grid[row][col] == 'O') for (row2 in row + 1..grid.size) {
                if (row2 == grid.size || grid[row2][col] != '.') {
                    val x = grid[row][col]
                    grid[row][col] = grid[row2 - 1][col]
                    grid[row2 - 1][col] = x
                    break
                }
            }
        }
    }
}

private fun tiltEast(grid: Input) {
    for (col in grid[0].size - 1 downTo 0) {
        for (row in grid.indices) {
            if (grid[row][col] == 'O') for (col2 in col + 1..grid[0].size) {
                if (col2 == grid[0].size || grid[row][col2] != '.') {
                    val x = grid[row][col]
                    grid[row][col] = grid[row][col2 - 1]
                    grid[row][col2 - 1] = x
                    break
                }
            }
        }
    }
}

private fun calcSum(grid: Input): Int {
    var result = 0
    var counter = grid.size
    grid.forEach {
        it.forEach { x -> if (x == 'O') result += counter }
        counter--
    }
    return result
}

private fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    val rows = mutableListOf<MutableList<Char>>()
    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        rows.add(rawLine.toMutableList())
    }
    return rows
}

private typealias Input = MutableList<MutableList<Char>>
