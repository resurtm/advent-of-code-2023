package com.resurtm.aoc2023.day05

fun launchDay05(testCase: String) {
    val env = readEnv(testCase)
    println("Day 05, part 01: ${solvePart1(env)}")
    println("Day 05, part 02: ${solvePart2(env)}")
}

private fun solvePart2(env: Env): Long? {
    var min: Long? = null
    for (rawPair in env.seedsV2) {
        for (raw in rawPair.first..<rawPair.first + rawPair.second) {
            var seed = raw
            for (trans in env.trans) {
                seed = doTrans(seed, trans)
            }
            if (min == null || min > seed) {
                min = seed
            }
        }
    }
    return min
}

private fun solvePart1(env: Env): Long? {
    var min: Long? = null
    for (raw in env.seeds) {
        var seed = raw
        for (trans in env.trans) {
            seed = doTrans(seed, trans)
        }
        if (min == null || min > seed) {
            min = seed
        }
    }
    return min
}

private fun doTrans(seed: Long, trans: List<TransItem>): Long {
    for (tr in trans) {
        if (seed in tr.src..<tr.src + tr.len) {
            return seed + (tr.dst - tr.src)
        }
    }
    return seed
}

private fun readEnv(testCase: String): Env {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    val seeds = mutableListOf<Long>()
    val trans = mutableListOf<List<TransItem>>()

    var transItem = mutableListOf<TransItem>()
    var readMap = false

    while (true) {
        val line = reader.readLine() ?: break
        if (readMap) {
            if (line.isEmpty()) {
                trans.add(transItem)
                transItem = mutableListOf()
                readMap = false
                continue
            }

            val raw = line.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toLong() }
            transItem.add(TransItem(dst = raw[0], src = raw[1], len = raw[2]))
            continue
        }

        if (line.contains("seeds: ")) {
            seeds.addAll(line.split(":")[1].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                .map { it.toLong() })
            continue
        }

        if (line.contains(" map:")) {
            readMap = true
            continue
        }
    }

    if (transItem.isNotEmpty()) {
        trans.add(transItem)
    }

    val seedsV2 = seeds.chunked(2).map { Pair(it[0], it[1]) }
    return Env(seeds = seeds, seedsV2 = seedsV2, trans = trans)
}

private data class Env(val seeds: List<Long>, val seedsV2: List<Pair<Long, Long>>, val trans: List<List<TransItem>>)

private data class TransItem(val dst: Long, val src: Long, val len: Long)
