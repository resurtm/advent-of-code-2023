package com.resurtm.aoc2023.day15

fun launchDay15(testCase: String) {
    val input = readInput(testCase)
    println("Day 15, part 1: ${input.sumOf { calcHash(it) }}")
    println("Day 15, part 2: ${calcPart2(input)}")
}

private fun calcPart2(items: List<String>): Int {
    val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
    items.forEach { item ->
        if (item.indexOf('=') != -1) {
            val p = item.split('=')
            val id = calcHash(p[0])
            if (!boxes.containsKey(id)) boxes[id] = mutableListOf()
            val box = boxes[id]
            if (box != null) {
                val pos = box.indexOfFirst { it.first == p[0] }
                if (pos == -1)
                    box.add(Pair(p[0], p[1].toInt()))
                else
                    box[pos] = Pair(p[0], p[1].toInt())
            }
        } else if (item.indexOf('-') != -1) {
            val p = item.split('-')
            val id = calcHash(p[0])
            if (!boxes.containsKey(id)) boxes[id] = mutableListOf()
            val box = boxes[id]
            if (box != null) {
                val pos = box.indexOfFirst { it.first == p[0] }
                if (pos != -1)
                    boxes[id] = (box.subList(0, pos) + box.subList(pos + 1, box.size)).toMutableList()
            }
        }
    }
    var result = 0
    boxes.forEach { box ->
        box.value.forEachIndexed { idx, item ->
            result += (box.key + 1) * (idx + 1) * item.second
        }
    }
    return result
}

private fun calcHash(str: String): Int {
    var curr = 0
    str.forEach {
        curr += it.code
        curr *= 17
        curr %= 256
    }
    return curr
}

private fun readInput(testCase: String): List<String> {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Invalid state, cannot read the input")
    val rawLine = (reader.readLine() ?: return emptyList()).trim()
    return rawLine.split(',')
}