package com.resurtm.aoc2023.day22

internal fun solvePart1(testCase: String): Long {
    val origin = Space()
    origin.readBricks(testCase)
    origin.applyGravity()

    var result = 0L
    for (b in origin.bricks.indices) {
        val cloned0 = origin.deepCopy()
        val cloned1 = origin.deepCopy()

        cloned0.bricks.removeAt(b)
        cloned1.bricks.removeAt(b)
        cloned1.applyGravity()

        if (cloned0 == cloned1)
            result++
    }
    return result
}
