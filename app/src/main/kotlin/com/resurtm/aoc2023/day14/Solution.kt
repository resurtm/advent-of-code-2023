package com.resurtm.aoc2023.day14

fun launchDay14(testCase: String) {
    launchPart1(testCase)
    launchPart2(testCase)
}

private fun launchPart1(testCase: String) {
    val input = readInput(testCase)
    tiltNorth(input)
    println("Day 14, part 1: ${calcSum(input)}")
}

private fun launchPart2(testCase: String) {
    val grid = readInput(testCase)
    var tilts = 0
    val history = mutableListOf<Input>()

    while (search(grid, history) == -1) {
        tilts++
        history.add(clone(grid))

        tiltNorth(grid)
        tiltWest(grid)
        tiltSouth(grid)
        tiltEast(grid)
    }

    val goal = 1_000_000_000
    val pos = search(grid, history)
    val diff = tilts - pos
    var todo = (goal - pos) % diff

    do {
        tiltNorth(grid)
        tiltWest(grid)
        tiltSouth(grid)
        tiltEast(grid)

        todo--
    } while (todo > 0)

    println("Day 14, part 2: ${calcSum(grid)}")
}

private fun clone(input: Input): Input = input.map { it.toMutableList() }.toMutableList()

private fun search(a: Input, bs: List<Input>): Int {
    bs.forEachIndexed { idx, b ->
        var res = true
        loop@ for (row in a.indices) for (col in a[row].indices) {
            if (a[row][col] != b[row][col]) {
                res = false
                break@loop
            }
        }
        if (res) return idx
    }
    return -1
}

private fun printInput(grid: Input) {
    println("-----")
    grid.forEach { println(it.joinToString("")) }
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
