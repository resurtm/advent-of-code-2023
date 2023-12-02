package com.resurtm.aoc2023.day01

fun launchDay01(testCase: String) {
    launchInternal(testCase, ::parseLineDay01)
    launchInternal(testCase, ::parseLineDay02)
}

private fun parseLineDay01(line: String): Int {
    var result = ""
    for (ln in arrayOf(line, line.reversed())) {
        for (ch in ln.iterator()) {
            if (ch in '1'..'9') {
                result += ch
                break
            }
        }
    }
    return if (result.isEmpty()) 0 else result.toInt()
}

private val validTokens = arrayOf(
    Pair("1", 1), Pair("2", 2), Pair("3", 3), Pair("4", 4), Pair("5", 5),
    Pair("6", 6), Pair("7", 7), Pair("8", 8), Pair("9", 9),
    Pair("one", 1), Pair("two", 2), Pair("three", 3), Pair("four", 4), Pair("five", 5),
    Pair("six", 6), Pair("seven", 7), Pair("eight", 8), Pair("nine", 9),
);

private fun parseLineDay02(line: String): Int {
    var firstPos = -1
    var first = validTokens[0]

    var lastPos = -1
    var last = validTokens[0]

    for (token in validTokens) {
        val firstPosition = line.indexOf(token.first)
        if (firstPosition != -1 && (firstPos == -1 || firstPos > firstPosition)) {
            firstPos = firstPosition
            first = token
        }

        val lastPosition = line.lastIndexOf(token.first)
        if (lastPosition != -1 && (lastPos == -1 || lastPos < lastPosition)) {
            lastPos = lastPosition
            last = token
        }
    }

    return first.second * 10 + last.second
}

private fun launchInternal(testCase: String, parseLine: (String) -> Int) {
    val accum = mutableListOf<Int>()
    val reader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader() ?: return
    while (true) {
        accum.add(parseLine(reader.readLine() ?: break))
    }
    println(accum.reduce { acc, i -> acc + i })
}
