package com.resurtm.aoc2023

fun launchDay01(testCase: String) {
    val calib = mutableListOf<Int>()

    fun parseLine(line: String) {
        var calibItem = ""
        for (ln in arrayOf(line, line.reversed())) {
            for (ch in ln.iterator()) {
                if (ch in '1'..'9') {
                    calibItem += ch
                    break
                }
            }
        }
        calib.add(calibItem.toInt())
    }

    val reader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader() ?: return
    while (true) {
        parseLine(reader.readLine() ?: break)
    }

    println(calib.reduce { acc, i -> acc + i })
}
