package com.resurtm.aoc2023.day09

fun launchDay09(testCase: String) {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read the input")
    var res1 = 0
    var res2 = 0
    while (true) {
        val res = parseLine(reader.readLine() ?: break)
        res1 += res.first
        res2 += res.second
    }
    println("Day 09, part 1: $res1")
    println("Day 09, part 2: $res2")
}

private fun parseLine(line: String): Pair<Int, Int> {
    var items = line.split(' ').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }
    val accum = mutableListOf<Int>()
    val accumV2 = mutableListOf<Int>()
    while (!items.all { it == 0 }) {
        accum.add(items.last())
        accumV2.add(items.first())
        val nextItems = mutableListOf<Int>()
        for (idx in 0..items.size - 2)
            nextItems.add(items[idx + 1] - items[idx])
        items = nextItems
    }
    return Pair(accum.sum(), accumV2.reversed().reduce { acc, i -> i - acc })
}
