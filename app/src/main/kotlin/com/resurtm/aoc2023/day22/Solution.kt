package com.resurtm.aoc2023.day22

fun launchDay22(testCase: String) {
    println(readInput(testCase))
}

internal fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read an input, probably it is invalid")

    val bricks = mutableListOf<Brick>()
    while (true) {
        val rawLine = reader.readLine()
            ?: break
        if (rawLine.trim().isEmpty())
            continue

        val parts0 = rawLine.split('~').map { it.trim() }
        val parts1 = parts0[0].split(',').map { it.trim().toLong() }
        val parts2 = parts0[1].split(',').map { it.trim().toLong() }

        val a = Pos(x = parts1[0], y = parts1[1], z = parts1[2])
        val b = Pos(x = parts2[0], y = parts2[1], z = parts2[2])
        bricks.add(Brick(a, b))
    }
    return Input(bricks)
}

internal data class Pos(val x: Long, val y: Long, val z: Long)

internal data class Brick(val a: Pos, val b: Pos)

internal data class Input(val br: List<Brick>)
