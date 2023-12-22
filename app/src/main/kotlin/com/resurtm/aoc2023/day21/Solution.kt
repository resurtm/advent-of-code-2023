package com.resurtm.aoc2023.day21

// import kotlin.math.floor

/**
 * See the README.md file for more details on this one.
 */
fun launchDay21(testCase: String) {
    val part1 = readInput(testCase).traverse(64L).coords.size
    println("Day 21, part 1: $part1")

    /*
    for (steps in 0L..<1000L) {
        val res = readInput(testCase).traverse(steps, useInf = true)

//        if () {
//            break
//        }
        // val fl = floor(res.coords.size.toDouble() / res.clampedCoords.size.toDouble()).toLong() - 1
        println("${steps}, ${res.coords.size}")
    }
    */
}
