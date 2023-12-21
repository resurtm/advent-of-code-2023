package com.resurtm.aoc2023.day21

fun launchDay21(testCase: String) {
    val part1 = traverse(readInput(testCase), 64L).size
    println("Day 21, part 1: $part1")
}
