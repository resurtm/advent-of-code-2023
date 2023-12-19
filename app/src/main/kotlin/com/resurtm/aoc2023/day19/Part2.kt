package com.resurtm.aoc2023.day19

import java.util.ArrayDeque

internal fun solvePart2(input: Input): Long {
    val result = ArrayDeque<Slice>()
    val queue = ArrayDeque<Slice>()
    queue.add(
        Slice(rule = "in", x = 1L..4000L, m = 1L..4000L, a = 1L..4000L, s = 1L..4000L)
    )

    queue@ while (queue.isNotEmpty()) {
        var slice = queue.removeFirst() ?: throw Exception("A slice is expected")
        if (slice.rule in arrayOf("A", "R")) {
            result.add(slice)
            continue@queue
        }

        val rule = input.rules[slice.rule] ?: throw Exception("A rule does not exist")
        for (condition in rule.conditions) {
            when (condition) {
                is Condition.Full -> {
                    val split = slice.split(condition)
                    if (!split.first.isEmpty()) queue.add(split.first)
                    if (split.second.isEmpty()) continue@queue
                    else slice = split.second
                }

                is Condition.Short -> queue.add(slice.copy(rule = condition.nextRule))
            }
        }
    }

    return result.filter { it.rule == "A" }.fold(0L) { acc, x -> acc + x.findSum() }
}
