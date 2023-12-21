package com.resurtm.aoc2023.day21

internal fun traverse(grid: Grid, maxSteps: Long, useInf: Boolean = false): Set<Pos> {
    var coords = mutableSetOf(grid.findStart())
    var steps = 0L

    while (steps < maxSteps) {
        val nextCoords = mutableSetOf<Pos>()
        coords.forEach {
            nextCoords.addAll(
                if (useInf) grid.findNextCoordsInf(it)
                else grid.findNextCoords(it)
            )
        }

        coords = nextCoords
        steps++
    }

    return coords
}
