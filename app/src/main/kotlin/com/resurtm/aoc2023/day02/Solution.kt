package com.resurtm.aoc2023.day02

fun launchDay02(testCase: String) {
    var result = 0
    val reader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader() ?: return
    while (true) {
        val res = parseLine(reader.readLine() ?: break)
        if (res.second) {
            result += res.first
        }
    }
    println("Part 1 answer: $result")
}

private fun parseLine(line: String): Pair<Int, Boolean> {
    val parts = line.split(":")
    val gameNum = parts[0].trim().split(" ")[1].trim().toInt()
    for (reveal in parts[1].trim().split(";")) {
        var red = 12
        var green = 13
        var blue = 14
        for (rawCubes in reveal.trim().split(",")) {
            val cubes = rawCubes.trim().split(" ")
            when (cubes[1]) {
                "red" -> {
                    red -= cubes[0].toInt()
                    if (red < 0) return Pair(gameNum, false)
                }

                "green" -> {
                    green -= cubes[0].toInt()
                    if (green < 0) return Pair(gameNum, false)
                }

                "blue" -> {
                    blue -= cubes[0].toInt()
                    if (blue < 0) return Pair(gameNum, false)
                }
            }
        }
    }
    return Pair(gameNum, true)
}
