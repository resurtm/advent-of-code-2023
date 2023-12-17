package com.resurtm.aoc2023.day17

fun launchDay17(testCase: String) {
    val grid = readInputGrid(testCase)
    printGrid(grid)
}

private fun printGrid(gr: Grid) {
    println("=====")
    gr.forEachIndexed { row, rowItems ->
        println(rowItems.map { it.toString() }.joinToString(""))
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
