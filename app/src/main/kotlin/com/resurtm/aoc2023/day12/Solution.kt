package com.resurtm.aoc2023.day12

fun launchDay12(testCase: String) {
    println("Day 12, part 1: ${calcPart1(testCase)}")
}

private fun calcPart1(testCase: String): Int {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    var result = 0
    while (true) {
        val rawLine = reader.readLine() ?: break
        println(rawLine)
        val parsed = parseLine(rawLine)
        val res = mutableSetOf<String>()
        combine(parsed.first, "#", parsed.second, res, compCount = 5)
        combine(parsed.first, ".", parsed.second, res, compCount = 5)
        result += res.size
        println(res.size)
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

private fun combine(mask: String, ch: String, blocks: List<Int>, result: MutableSet<String>, compCount: Int = 1) {
    val pos = mask.indexOf("?")
    if (pos == -1) {
        if (checkMask(mask, blocks, compCount)) result.add(mask)
        return
    }
    val nextMask = mask.substring(0, pos) + ch + mask.substring(pos + 1, mask.length)
    combine(nextMask, "#", blocks, result, compCount)
    combine(nextMask, ".", blocks, result, compCount)
}

private fun checkMask(inpMask: String, inpBlocks: List<Int>, compCount: Int = 1): Boolean {
    var block = 0
    var accum = 0

    val mask = inpMask
    val blocks = inpBlocks

    /*
    val mask = mutableListOf<Char>()
    val blocks = mutableListOf<Int>()
    repeat(compCount) {
        mask += inpMask
        if (it != compCount - 1) mask.add('?')
        blocks += inpBlocks
    }

    combine(mask, '#', blocks, result, compCount)
    combine(mask, '.', blocks, result, compCount)*/

    (mask + '.').forEach {
        if (it == '.') {
            if (accum != 0) {
                if (block == blocks.size || accum != blocks[block]) return false
                block++
            }
            accum = 0
        } else accum++
    }
    return block == blocks.size
}

private fun parseLineTemp(rawLine: String) {
    val parts = rawLine.split(' ')
    val mask = parts[0].toMutableList()
    val blocks = parts[1].split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

    traverse(0, mask + '.', blocks)
//    println(total)
//    println("=====")
}

private fun traverse(startAt: Int, mask: List<Char>, blocks: List<Int>, depth: Int = 0, pos: List<Int> = listOf()) {
    if (blocks.isEmpty()) {
        if (compare(blocks, pos, mask)) println("$depth - $pos")
        return
    }
    val block = blocks[0]
    if (mask.subList(startAt, mask.size).isEmpty()) return

    for (idx in startAt..<mask.size - block) {
        val chunk = mask.subList(idx, idx + block + 1)
        if (!(chunk.subList(0, chunk.size - 1).all { it == '?' || it == '#' } && chunk.last() != '#')) continue

        traverse(
            idx + block + 1,
            mask,
            blocks.subList(1, blocks.size),
            depth + 1,
            pos + listOf(idx)
        )
    }
}

private fun compare(blocks: List<Int>, pos: List<Int>, mask: List<Char>): Boolean {
    val newMask = mutableListOf<Char>()
    mask.forEach { _ -> newMask.add('.') }
    for (idx in pos.indices) {
        repeat(blocks[idx]) {
            newMask[pos[idx] + it] = '#'
        }
    }
    println(newMask)
    return true
}
