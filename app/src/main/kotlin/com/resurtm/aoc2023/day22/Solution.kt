package com.resurtm.aoc2023.day22

fun launchDay22(testCase: String) {
    val solution = solve(testCase)
    println("Day 22, part 1: ${solution.first}")
    println("Day 22, part 2: ${solution.second}")
}

internal fun solve(testCase: String): Pair<Long, Long> {
    val origin = Space()
    origin.readBricks(testCase)
    origin.applyGravity()

    var canBeRemoved = 0L
    var moved = 0L

    for (b0 in origin.bricks.indices) {
        val cloned0 = origin.deepCopy()
        val cloned1 = origin.deepCopy()

        cloned0.bricks.removeAt(b0)
        cloned1.bricks.removeAt(b0)
        cloned1.applyGravity()

        if (cloned0 == cloned1)
            canBeRemoved++

        for (b1 in cloned0.bricks.indices) {
            if (cloned0.bricks[b1] != cloned1.bricks[b1])
                moved++
        }
    }

    return Pair(canBeRemoved, moved)
}
