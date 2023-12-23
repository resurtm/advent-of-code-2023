package com.resurtm.aoc2023.day23

import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun canSolvePart1() {
        val expected = 94
        val actual = grid().solvePart1()
        assertEquals(expected, actual)
    }

    @Test
    fun canSolvePart2() {
        val expected = 154
        val actual = grid().solvePart2()
        assertEquals(expected, actual)
    }

    @Test
    fun testFindNextPositionsV3() {
        val expected = listOf(Pos(17, 13), Pos(19, 15), Pos(19, 11))
        val actual = grid().findNextPositionsV3(Pos(19, 13))
        assertEquals(expected, actual)
    }

    private fun grid() = Grid.readInput("/day23/test-case.txt")
}
