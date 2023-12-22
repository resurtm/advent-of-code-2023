package com.resurtm.aoc2023.day22

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day22Test {
    @Test
    fun canSolveIt() {
        val expected = Pair(5L, 7L)
        val actual = solve("/day22/test-case.txt")
        assertEquals(expected, actual)
    }

    @Test
    fun canCheckBrickZIntersection() {
        val brick0 = Brick(Pos(1L, 1L, 1L), Pos(10L, 10L, 1L))
        val brick1 = Brick(Pos(10L, 10L, 100L), Pos(20L, 20L, 100L))
        val brick2 = brick1.copy(a = brick1.a.copy(x = 11L))

        // case 1
        assertTrue { brick0.intersectsZ(brick1) }
        assertTrue { brick1.intersectsZ(brick0) }

        // case 2
        assertFalse { brick0.intersectsZ(brick2) }
        assertFalse { brick2.intersectsZ(brick0) }

        assertTrue { brick2.intersectsZ(brick1) }
        assertTrue { brick1.intersectsZ(brick2) }

        // case 3
        assertTrue { brick0.intersectsZ(brick0) }
        assertTrue { brick1.intersectsZ(brick1) }
        assertTrue { brick2.intersectsZ(brick2) }
    }
}
