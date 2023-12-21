package com.resurtm.aoc2023.day21

fun launchDay21(testCase: String) {
    /*val part1 = traverse(readInput(testCase), 64L).size
    println("Day 21, part 1: $part1")*/

    val history = mutableListOf<Set<Pos>>()

    for (steps in 1L..200L) {
        val res = readInput(testCase).traverse(steps, useInf = true)
        history.add(res)

        println("$steps - ${res.size}")
    }
}
