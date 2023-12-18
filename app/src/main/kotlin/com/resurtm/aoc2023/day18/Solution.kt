package com.resurtm.aoc2023.day18

fun launchDay18(testCase: String) {
    val moves = readMoves(testCase)

    val part1 = solvePart1(moves)
    println("Day 18, part 1: $part1")

    val part2 = solvePart2(moves)
    println("Day 18, part 2: $part2")
}
