package com.resurtm.aoc2023.day18

fun solvePart2(moves: List<Move>): Int {
    var curr = Pos()
    for (move in moves) {
        curr = getNextPos(curr, move)
    }
    return 0
}
