package com.resurtm.aoc2023.day18

import kotlin.math.abs

/**
 * https://en.wikipedia.org/wiki/Shoelace_formula
 * https://artofproblemsolving.com/wiki/index.php/Shoelace_Theorem
 */
fun solvePart2Good(moves: List<Move>): Long {
    val lines = mutableListOf<Line>()
    val vertices = mutableListOf<Pos>()
    var borderArea = 0L
    var currPos = Pos()

    for (move in moves) {
        vertices.add(currPos)

        val nextPos = getNextPosV2(currPos, move)
        val newLine = Line(currPos, nextPos)
        var addArea = newLine.length

        lines.forEach {
            if (newLine.intersects(it)) addArea--
        }

        borderArea += addArea
        currPos = nextPos
        lines.add(newLine)
    }

    vertices.add(vertices.first())

    return calcShoelace(vertices) + borderArea / 2 + 1
}

/**
 * https://en.wikipedia.org/wiki/Shoelace_formula
 * https://artofproblemsolving.com/wiki/index.php/Shoelace_Theorem
 */
private fun calcShoelace(vertices: List<Pos>): Long {
    var res = 0L
    for (idx in 0..vertices.size - 2) {
        val v0 = vertices[idx]
        val v1 = vertices[idx + 1]
        res += (v1.row + v0.row) * (v1.col - v0.col)
    }
    return abs(res / 2)
}
