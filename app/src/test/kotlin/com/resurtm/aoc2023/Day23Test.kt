package com.resurtm.aoc2023.day23

import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun canSolvePart1() {
        val expected = 94
        val actual = Grid.readInput("/day23/test-case.txt").solvePart1()
        assertEquals(expected, actual)
    }

    @Test
    fun canSolvePart2() {
        val expected = 154
        val actual = Grid.readInput("/day23/test-case.txt").solvePart2()
        assertEquals(expected, actual)
    }
}
