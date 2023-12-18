package com.resurtm.aoc2023.day18

fun launchDay18(testCase: String) {
    println(readMoves(testCase))
}

fun solvePart1() {
}

fun readMoves(testCase: String): List<Move> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read an input")
    val moves = mutableListOf<Move>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        val rawParts = rawLine.trim().split(' ')
        val dir = when (rawParts[0][0]) {
            'U' -> Dir.U
            'D' -> Dir.D
            'L' -> Dir.L
            'R' -> Dir.R
            else -> throw Exception("Invalid state, an unknown direction")
        }
        val len = rawParts[1].toInt()
        val color = rawParts[2].trimStart('(', '#').trimEnd(')')
        moves.add(Move(dir, len, color))
    }
    return moves
}

data class Move(val dir: Dir, val len: Int, val color: String)

enum class Dir { U, D, L, R }
