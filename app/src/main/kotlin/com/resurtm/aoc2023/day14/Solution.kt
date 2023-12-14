package com.resurtm.aoc2023.day14

fun launchDay14(testCase: String) {
    val input = readInput(testCase)
    for (row in input.indices) {
        for (col in input[row].indices) {
            if (input[row][col] == 'O') {
                for (row2 in row - 1 downTo -1) {
                    if (row2 == -1 || input[row2][col] != '.') {
                        val x = input[row][col]
                        input[row][col] = input[row2 + 1][col]
                        input[row2 + 1][col] = x
                        break
                    }
                }
            }
        }
    }
    var result = 0
    var counter = input.size
    input.forEach {
        it.forEach { if (it == 'O') result += counter }
        counter--
    }
    println(result)
    // input.forEach { println(it.joinToString("")) }
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
