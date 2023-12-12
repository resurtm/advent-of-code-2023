package com.resurtm.aoc2023.day12

fun launchDay12(testCase: String) {
//    parseLine("?.? 1,1")
//    parseLine("???.### 1,1,3")
//     parseLine(".??..??...?##. 1,1,3")
//     parseLine("?#?#?#?#?#?#?#? 1,3,1,6")
//     parseLine("????.#...#... 4,1,1")
    parseLine("?###???????? 3,2,1")

//    val reader =
//        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
//            ?: throw Exception("Invalid state, cannot read the input")
//
//    while (true) {
//        val rawLine = reader.readLine() ?: break
//        parseLine(rawLine)
//    }
}

private fun parseLine(rawLine: String) {
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
