package com.resurtm.aoc2023.day21

import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun canSolvePart1() {
        val expected = setOf(
            Pos(row = 3, col = 5),
            Pos(row = 5, col = 5),
            Pos(row = 3, col = 7),
            Pos(row = 6, col = 4),
            Pos(row = 5, col = 3),
            Pos(row = 2, col = 8),
            Pos(row = 4, col = 8),
            Pos(row = 7, col = 3),
            Pos(row = 7, col = 5),
            Pos(row = 3, col = 3),
            Pos(row = 4, col = 2),
            Pos(row = 9, col = 3),
            Pos(row = 7, col = 1),
            Pos(row = 6, col = 6),
            Pos(row = 3, col = 1),
            Pos(row = 4, col = 0),
        )
        val actual = getInput().traverse(6L)
        assertEquals(expected, actual)
    }

    @Test
    fun canSolvePart2() {
        mapOf(
            6L to 16L,
            10L to 50L,
            50L to 1594L,
            100L to 6536L,
            // 500L to 167004L,
            // 1000L to 668697L,
            // 5000L to 16733044L,
        ).forEach { (maxSteps, expected) ->
            val actual = getInput().traverse(maxSteps, useInf = true).size.toLong()
            assertEquals(expected, actual)
        }
    }

    private fun getInput() = readInput("/day21/test-case.txt")
}
