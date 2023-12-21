package com.resurtm.aoc2023.day21

internal fun solvePart1(grid: Grid, maxSteps: Long): Set<Pos> {
    var coords = mutableSetOf(grid.findStart())
    var steps = 0L

    while (steps < maxSteps) {
        val nextCoords = mutableSetOf<Pos>()
        coords.forEach {
            nextCoords.addAll(grid.findNextCoords(it))
        }

        coords = nextCoords
        steps++
    }

    return coords
}
