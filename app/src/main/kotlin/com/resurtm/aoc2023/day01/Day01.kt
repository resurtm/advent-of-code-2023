package com.resurtm.aoc2023.day01

fun launchDay01(testCase: String) {
    launchInternal(testCase)
}

private fun launchInternal(testCase: String) {
    val accum = mutableListOf<Int>()

    fun parseLine(line: String) {
        var newItem = ""
        for (ln in arrayOf(line, line.reversed())) {
            for (ch in ln.iterator()) {
                if (ch in '1'..'9') {
                    newItem += ch
                    break
                }
            }
        }
        accum.add(newItem.toInt())
    }

    val reader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader() ?: return
    while (true) {
        parseLine(reader.readLine() ?: break)
    }

    println(accum.reduce { acc, i -> acc + i })
}
