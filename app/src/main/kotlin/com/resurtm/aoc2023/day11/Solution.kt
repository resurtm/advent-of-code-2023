package com.resurtm.aoc2023.day11

import kotlin.math.abs

fun launchDay11(testCase: String) {
    println("===== NEW IMPL =====")
    runNewImpl(testCase)
    println("===== OLD IMPL =====")
    runOldImpl(testCase)
}

private fun runNewImpl(testCase: String) {
    mapOf(1 to 2, 2 to 1_000_000, 3 to 10, 4 to 100).forEach { (case, expandSize) ->
        val result = runNewImplInternal(testCase, expandSize)
        println("Day 11, part $case, expand $expandSize: $result")
    }
}

private fun runNewImplInternal(testCase: String, extraSpace: Int): Long {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    var rawRow = 0
    var extraRow = 0L
    val stars = mutableListOf<Pair<Long, Long>>()
    var cols = mutableListOf<MutableList<Boolean>>()

    while (true) {
        val rawLine = reader.readLine() ?: break
        if (cols.isEmpty()) cols = MutableList(rawLine.length) { mutableListOf() }
        if (rawLine.indexOf('#') == -1) extraRow += extraSpace - 1
        rawLine.forEachIndexed { col, ch ->
            cols[col].add(ch == '#')
            if (ch == '#') stars.add(Pair(extraRow, col.toLong()))
        }
        rawRow += 1
        extraRow += 1L
    }

    var extraCol = 0L
    cols.forEachIndexed { index, col ->
        if (col.indexOf(true) == -1) {
            stars.forEachIndexed { idx, it ->
                if (it.second >= extraCol) stars[idx] = Pair(it.first, it.second + extraSpace - 1)
            }
            extraCol += extraSpace - 1
        }
        extraCol += 1L
    }
    // println(stars)

    var result = 0L
    for (s1 in 0..<stars.size) {
        for (s2 in s1 + 1..<stars.size) {
            result += abs(stars[s1].first - stars[s2].first)
            result += abs(stars[s1].second - stars[s2].second)
        }
    }
    return result
    // println(result)
}

private fun runOldImpl(testCase: String) {
    mapOf(1 to 2, 3 to 10, 4 to 100).forEach { (case, expandSize) ->
        val input = readStars(testCase)
        expandRowsSpace(input.stars, expandSize)
        expandColsSpace(input.stars, expandSize)
        val dist1 = calcDists1(input.stars)
        val dist2 = calcDists2(input.stars)
        var result = 0L
        for (star1 in 0..<input.maxStar) {
            for (star2 in star1..<input.maxStar) {
                result += dist1[Pair(star1, star2)] ?: 0L
                result += dist2[Pair(star1, star2)] ?: 0L
            }
        }
        println("Day 11, part $case, expand $expandSize: $result")
    }
}

private fun calcDists2(stars: Stars): MutableMap<Pair<Long, Long>, Long> {
    val seen = mutableMapOf<Long, Long>()
    val dists = mutableMapOf<Pair<Long, Long>, Long>()
    for (col in 0..<stars[0].size) {
        for (row in 0..<stars.size) {
            val star = stars[row][col]
            if (star != -1L) {
                seen.forEach { (otherStar, otherPos) ->
                    dists[Pair(star, otherStar)] = abs(row - otherPos)
                    dists[Pair(otherStar, star)] = abs(row - otherPos)
                }
                seen[star] = row.toLong()
            }
        }
    }
    return dists
}

private fun calcDists1(stars: Stars): MutableMap<Pair<Long, Long>, Long> {
    val seen = mutableMapOf<Long, Long>()
    val dists = mutableMapOf<Pair<Long, Long>, Long>()
    stars.forEach { row ->
        row.forEachIndexed { pos, star ->
            if (star != -1L) {
                seen.forEach { (otherStar, otherPos) ->
                    dists[Pair(star, otherStar)] = abs(pos - otherPos)
                    dists[Pair(otherStar, star)] = abs(pos - otherPos)
                }
                seen[star] = pos.toLong()
            }
        }
    }
    return dists
}

private fun expandRowsSpace(stars: Stars, expandSize: Int = 2) {
    var row = 0
    do {
        if (!hasStars(stars[row])) {
            repeat(expandSize - 1) {
                stars.add(row, MutableList(stars[row].size) { -1 })
                row++
            }
        }
        row++
    } while (row < stars.size)
}

private fun expandColsSpace(stars: Stars, expandSize: Int = 2) {
    var col = 0L
    do {
        val items = mutableListOf<Long>()
        stars.forEach { items.add(it[col.toInt()]) }
        if (!hasStars(items)) {
            repeat(expandSize - 1) {
                stars.forEach { it.add(col.toInt(), -1L) }
                col++
            }
        }
        col++
    } while (col < stars.first().size)
}

private fun hasStars(items: MutableList<Long>): Boolean = items.find { it != -1L } != null

private fun printStars(stars: Stars) {
    println("=====")
    stars.forEach { row ->
        row.forEach { print(if (it == -1L) '.' else '#') }
        println()
    }
}

private fun readStars(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    var starIndex = 0L
    val stars = mutableListOf<MutableList<Long>>()
    while (true) {
        val rawLine = reader.readLine() ?: break
        val row = rawLine.map {
            if (it == '#') starIndex++ else -1
        }
        stars.add(row.toMutableList())
    }
    return Input(stars, maxStar = starIndex)
}

private data class Input(val stars: Stars, val maxStar: Long)

private typealias Stars = MutableList<MutableList<Long>>
