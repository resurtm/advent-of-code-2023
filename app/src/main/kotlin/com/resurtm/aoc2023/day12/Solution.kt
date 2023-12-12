package com.resurtm.aoc2023.day12

fun launchDay12(testCase: String) {
    println("Day 12, part 1: ${calculate(testCase)}")
    println("Day 12, part 2: ${calculate(testCase, 5)}")
}

private fun calculate(testCase: String, repCount: Int = 1): Long {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    var result = 0L
    while (true) {
        val rawLine = reader.readLine() ?: break
        val p = parseLine(rawLine, repCount)
        val cache = mutableMapOf<Pair<String, List<Int>>, Int>()
        val item = comb(p.first, p.second, cache)
        result += item
    }
    return result
}

private fun parseLine(rawLine: String, repCount: Int = 1): Pair<String, List<Int>> {
    val parts = rawLine.split(' ')
    val rawMask = parts[0]
    val rawBlocks = parts[1].split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

    var mask = ""
    val blocks = mutableListOf<Int>()
    repeat(repCount) {
        mask += rawMask
        if (it != repCount - 1) mask += "?"
        blocks += rawBlocks
    }

    return Pair(mask, blocks)
}

private fun comb(
    mask: String,
    blocks: List<Int>,
    ca: MutableMap<Pair<String, List<Int>>, Int>
): Int {
    val existing = ca[Pair(mask, blocks)]
    if (existing != null) return existing

    if (mask.isEmpty()) {
        return if (blocks.isEmpty()) 1 else 0
    }

    return when (mask.first()) {
        '?' -> {
            comb(mask.replaceFirst('?', '#'), blocks, ca) +
                    comb(mask.replaceFirst('?', '.'), blocks, ca)
        }

        '.' -> {
            val res = comb(mask.trimStart('.'), blocks, ca)
            ca[Pair(mask, blocks)] = res
            res
        }

        '#' -> {
            if (blocks.isEmpty() || mask.length < blocks.first() || mask.substring(0, blocks.first())
                    .indexOf('.') != -1
            ) {
                ca[Pair(mask, blocks)] = 0
                0
            } else if (blocks.size > 1) {
                if (mask.length < blocks.first() + 1 || mask[blocks.first()] == '#') {
                    ca[Pair(mask, blocks)] = 0
                    0
                } else {
                    val res = comb(mask.substring(blocks.first() + 1, mask.length), blocks.subList(1, blocks.size), ca)
                    ca[Pair(mask, blocks)] = res
                    res
                }
            } else {
                val res = comb(mask.substring(blocks.first(), mask.length), blocks.subList(1, blocks.size), ca)
                ca[Pair(mask, blocks)] = res
                res
            }
        }

        else -> 0
    }
}
