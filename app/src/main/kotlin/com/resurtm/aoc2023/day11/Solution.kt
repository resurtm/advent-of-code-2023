package com.resurtm.aoc2023.day11

import kotlin.math.abs

fun launchDay11(testCase: String) {
    val stars = readStars(testCase)
    printStars(stars)
    expandRowsSpace(stars)
    expandColsSpace(stars)
    printStars(stars)
    println(calcVerticalDists(stars))
}

private fun calcVerticalDists(stars: Stars): MutableMap<Pair<Int, Int>, Int> {
    val seen = mutableMapOf<Int, Int>()
    val dists = mutableMapOf<Pair<Int, Int>, Int>()
    stars.forEach {
        it.forEachIndexed { pos, star ->
            if (star != -1) {
                seen.forEach { (otherStar, otherPos) ->
                    dists[Pair(star, otherStar)] = abs(pos - otherPos)
                    dists[Pair(otherStar, star)] = abs(pos - otherPos)
                }
                seen[star] = pos
            }
        }
    }
    return dists
}

private fun expandRowsSpace(stars: Stars) {
    var row = 0
    do {
        if (!hasStars(stars[row])) {
            repeat(1) {
                stars.add(row, MutableList(stars[row].size) { -1 })
                row++
            }
        }
        row++
    } while (row < stars.size)
}

private fun expandColsSpace(stars: Stars) {
    var col = 0
    do {
        val items = mutableListOf<Int>()
        stars.forEach { items.add(it[col]) }
        if (!hasStars(items)) {
            repeat(1) {
                stars.forEach { it.add(col, -1) }
                col++
            }
        }
        col++
    } while (col < stars.first().size)
}

private fun hasStars(items: MutableList<Int>): Boolean = items.find { it != -1 } != null

private fun printStars(stars: Stars) {
    println("=====")
    stars.forEach { row ->
        row.forEach { print(if (it == -1) '.' else '#') }
        println()
    }
}

private fun readStars(testCase: String): Stars {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    var starIndex = 0
    val stars = mutableListOf<MutableList<Int>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        val row = rawLine.map {
            if (it == '#') starIndex++ else -1
        }
        stars.add(row.toMutableList())
    }
    return stars
}

private typealias Stars = MutableList<MutableList<Int>>
