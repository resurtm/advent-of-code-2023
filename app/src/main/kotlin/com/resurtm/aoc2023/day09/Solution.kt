package com.resurtm.aoc2023.day09

fun launchDay09(testCase: String) {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    var res1 = 0
    while (true) {
        res1 += parseLine(reader.readLine() ?: break)
    }
    println("Day 09, part 1: $res1")
}

private fun parseLine(line: String): Int {
    var items = line.split(' ').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }
    val accum = mutableListOf<Int>()
    while (!items.all { it == 0 }) {
        accum.add(items.last())
        val nextItems = mutableListOf<Int>()
        for (idx in 0..items.size - 2) {
            nextItems.add(items[idx + 1] - items[idx])
        }
        items = nextItems
    }
    return accum.sum()
}
