package com.resurtm.aoc2023.day02

fun launchDay02(testCase: String) {
    var resultPart1 = 0
    var resultPart2 = 0
    val reader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader() ?: return
    while (true) {
        val res = parseLine(reader.readLine() ?: break)
        if (res.possible) {
            resultPart1 += res.gameNum
        }
        resultPart2 += res.power
    }
    println("Part 1 answer: $resultPart1")
    println("Part 2 answer: $resultPart2")
}

private fun parseLine(line: String): ParseLineResult {
    val parts = line.split(":")
    val gameNum = parts[0].trim().split(" ")[1].trim().toInt()

    var possible = true
    var maxRed = -1
    var maxGreen = -1
    var maxBlue = -1

    for (reveal in parts[1].trim().split(";")) {
        var red = 12
        var green = 13
        var blue = 14

        var redAccum = 0
        var greenAccum = 0
        var blueAccum = 0

        for (rawCubes in reveal.trim().split(",")) {
            val cubes = rawCubes.trim().split(" ")
            val value = cubes[0].trim().toInt()

            when (cubes[1]) {
                "red" -> {
                    redAccum += value
                    red -= value
                    if (red < 0) {
                        possible = false
                    }
                }

                "green" -> {
                    greenAccum += value
                    green -= value
                    if (green < 0) {
                        possible = false
                    }
                }

                "blue" -> {
                    blueAccum += value
                    blue -= value
                    if (blue < 0) {
                        possible = false
                    }
                }
            }
        }

        if (maxRed < redAccum) {
            maxRed = redAccum
        }
        if (maxBlue < blueAccum) {
            maxBlue = blueAccum
        }
        if (maxGreen < greenAccum) {
            maxGreen = greenAccum
        }
    }

    return ParseLineResult(gameNum, possible, maxRed * maxBlue * maxGreen)
}

private data class ParseLineResult(
    val gameNum: Int,
    val possible: Boolean,
    val power: Int,
)
