package com.resurtm.aoc2023.day07

fun launchDay07(testCase: String) {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    val result = listOf(mutableListOf<Hand>(), mutableListOf())
    while (true) {
        val line = reader.readLine() ?: break
        val cards = line.split(' ')[0].trim()
        val score = line.split(' ')[1].trim().toInt()

        main@ for (ver in 0..1) {
            val hand = Hand(cards = cards, score = score, version = ver + 1)
            if (result[ver].size == 0) {
                result[ver].add(hand)
                continue@main
            }
            for (idx in 0..<result[ver].size) {
                if (result[ver][idx] > hand) {
                    result[ver].add(idx, hand)
                    continue@main
                }
            }
            result[ver].add(hand)
        }
    }
    for (ver in 0..1) {
        val total = result[ver].foldIndexed(0) { index, acc, hand -> acc + (index + 1) * hand.score }
        println("Day 07, part ${ver + 1}: $total")
    }
}

private data class Hand(
    val cards: String, val score: Int, val version: Int = 1,
    private var assessment: Assessment = Assessment.UNKNOWN,
    private var lookup: Map<Char, Int> = emptyMap(),
) : Comparable<Hand> {
    init {
        assessment = if (version == 1) assessV1(cards) else assessV2(cards)
        lookup = if (version == 1) lookupV1 else lookupV2
    }

    override fun compareTo(other: Hand): Int {
        if (this.assessment.prio == other.assessment.prio) {
            for (idx in 0..<this.cards.length) {
                val a = lookup[this.cards[idx]]
                val b = lookup[other.cards[idx]]
                if (a != null && b != null && a > b) return 1
                if (a != null && b != null && a < b) return -1
            }
            return 0
        }
        return if (this.assessment.prio > other.assessment.prio) 1 else -1
    }

    private fun assessV1(inp: String): Assessment {
        val uniq = inp.toSet()
        val freq = mutableMapOf<Char, Int>()
        lookupV1.forEach { freq[it.key] = 0 }
        inp.forEach { val prev = freq[it]; freq[it] = if (prev == null) 0 else prev + 1 }
        val vals = freq.values.filter { it > 0 }.toSet()

        if (uniq.size == 1) return Assessment.FIVE_OF_A_KIND
        if (uniq.size == 2 && vals == setOf(4, 1)) return Assessment.FOUR_OF_A_KIND
        if (uniq.size == 2 && vals == setOf(3, 2)) return Assessment.FULL_HOUSE
        if (uniq.size == 3 && vals == setOf(3, 1, 1)) return Assessment.THREE_OF_A_KIND
        if (uniq.size == 3 && vals == setOf(2, 2, 1)) return Assessment.TWO_PAIR
        if (uniq.size == 4 && vals == setOf(2, 1, 1, 1)) return Assessment.ONE_PAIR
        return Assessment.HIGH_CARD
    }

    private fun assessV2(inp: String): Assessment {
        val pos = inp.indexOf('J')
        if (pos == -1) return assessV1(inp)
        var max = Assessment.UNKNOWN
        for (k in lookupV2Min.keys) {
            val res = assessV2(inp.substring(0, pos) + k + inp.substring(pos + 1, inp.length))
            if (max.prio < res.prio) max = res
        }
        return max
    }
}

private enum class Assessment(val prio: Int) {
    FIVE_OF_A_KIND(7), FOUR_OF_A_KIND(6), FULL_HOUSE(5), THREE_OF_A_KIND(4),
    TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1), UNKNOWN(-1),
}

private val lookupV1 = mapOf(
    'A' to 13, 'K' to 12, 'Q' to 11, 'J' to 10, 'T' to 9, '9' to 8,
    '8' to 7, '7' to 6, '6' to 5, '5' to 4, '4' to 3, '3' to 2, '2' to 1,
)
private val lookupV2Min = mapOf(
    'A' to 13, 'K' to 12, 'Q' to 11, 'T' to 10, '9' to 9, '8' to 8,
    '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2,
)
private val lookupV2 = lookupV2Min + mapOf('J' to 1)
