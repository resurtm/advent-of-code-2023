package com.resurtm.aoc2023.day07

fun launchDay07(testCase: String) {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex
    val result = mutableListOf<Hand>()

    main@ while (true) {
        val line = reader.readLine() ?: break
        val temp = line.split(' ')
        val hand = Hand(cards = temp[0].trim(), score = temp[1].trim().toInt())

        if (result.size == 0) {
            result.add(hand)
            continue@main
        }
        for (idx in 0..<result.size) {
            if (result[idx] > hand) {
                result.add(idx, hand)
                continue@main
            }
        }
        result.add(hand)
    }

    result.forEach { println("$it") }
}

private data class Hand(
    val cards: String, val score: Int, var assessment: Assessment = Assessment.UNKNOWN
) : Comparable<Hand> {
    init {
        assessment = assess()
    }

    override fun compareTo(other: Hand): Int {
        if (this.assessment.prio > other.assessment.prio) return 1
        return -1
    }

    private fun assess(): Assessment {
        val uniq = cards.toSet()
        val freq = mutableMapOf<Char, Int>()
        lookup.forEach { freq[it.key] = 0 }
        cards.forEach { val prev = freq[it]; freq[it] = if (prev == null) 0 else prev + 1 }
        val vals = freq.values.filter { it > 0 }.toSet()

        // 1. Assessment.FIVE_OF_A_KIND
        if (uniq.size == 1) return Assessment.FIVE_OF_A_KIND
        // 2. Assessment.FOUR_OF_A_KIND
        if (uniq.size == 2 && vals == setOf(4, 1)) return Assessment.FOUR_OF_A_KIND
        // 3. Assessment.FULL_HOUSE
        if (uniq.size == 2 && vals == setOf(3, 2)) return Assessment.FULL_HOUSE
        // 4. Assessment.THREE_OF_A_KIND
        if (uniq.size == 3 && vals == setOf(3, 1, 1)) return Assessment.THREE_OF_A_KIND
        // 5. Assessment.TWO_PAIR
        if (uniq.size == 3 && vals == setOf(2, 2, 1)) return Assessment.TWO_PAIR
        // 6. Assessment.TWO_PAIR
        if (uniq.size == 4 && vals == setOf(2, 1, 1, 1)) return Assessment.ONE_PAIR
        // 7. Assessment.FULL_HOUSE
        return Assessment.HIGH_CARD
    }
}

private val lookup = mapOf(
    'A' to 13, 'K' to 12, 'Q' to 11, 'J' to 10, 'T' to 9,
    '9' to 8, '8' to 7, '7' to 6, '6' to 5, '5' to 4, '4' to 3, '3' to 2, '2' to 1,
)

private enum class Assessment(val prio: Int) {
    FIVE_OF_A_KIND(7), FOUR_OF_A_KIND(6), FULL_HOUSE(5), THREE_OF_A_KIND(4),
    TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1), UNKNOWN(0),
}
