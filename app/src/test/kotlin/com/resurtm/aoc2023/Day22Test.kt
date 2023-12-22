package com.resurtm.aoc2023.day22

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day22Test {
    @Test
    fun canCheckBrickIntersection() {
        val brick0 = Brick(Pos(1L, 1L, 1L), Pos(10L, 10L, 1L))
        val brick1 = Brick(Pos(10L, 10L, 100L), Pos(20L, 20L, 100L))
        val brick2 = brick1.copy(a = brick1.a.copy(x = 11L))

        // case 1
        assertTrue { brick0.intersects(brick1) }
        assertTrue { brick1.intersects(brick0) }

        // case 2
        assertFalse { brick0.intersects(brick2) }
        assertFalse { brick2.intersects(brick0) }

        assertTrue { brick2.intersects(brick1) }
        assertTrue { brick1.intersects(brick2) }

        // case 3
        assertTrue { brick0.intersects(brick0) }
        assertTrue { brick1.intersects(brick1) }
        assertTrue { brick2.intersects(brick2) }
    }
}
