package com.resurtm.aoc2023.day12

fun launchDay12(testCase: String) {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")

    var result = 0
    while (true) {
        val rawLine = reader.readLine() ?: break
        result += parseLine(rawLine)
    }
    println("Day 12, part 1: $result")
}

private fun parseLine(rawLine: String): Int {
    val parts = rawLine.split(' ')
    val mask = parts[0].toMutableList()
    val blocks = parts[1].split(',').map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }

    val result = mutableSetOf<String>()
    combine(mask, '#', blocks, result)
    combine(mask, '.', blocks, result)
    return result.size
}

private fun combine(mask: List<Char>, ch: Char, blocks: List<Int>, result: MutableSet<String>) {
    val pos = mask.indexOf('?')
    if (pos == -1) {
        if (checkMask(mask, blocks)) result.add(mask.joinToString(""))
        return
    }
    val nextMask = mask.subList(0, pos) + ch + mask.subList(pos + 1, mask.size)
    combine(nextMask, '#', blocks, result)
    combine(nextMask, '.', blocks, result)
}

private fun checkMask(mask: List<Char>, blocks: List<Int>): Boolean {
    var block = 0
    var accum = 0
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
