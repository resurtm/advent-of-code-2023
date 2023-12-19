package com.resurtm.aoc2023.day19

import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun canParseRule() {
        val expected = Rule(
            name = "px",
            conditions = listOf(
                Condition.Full(token = Token.A, compOp = CompOp.Less, compVal = 2006, nextRule = "qkq"),
                Condition.Full(token = Token.M, compOp = CompOp.Greater, compVal = 2090, nextRule = "A"),
                Condition.Short(nextRule = "rfg"),
            ),
        )
        val actual = parseRule("px{a<2006:qkq,m>2090:A,rfg}")
        assertEquals(expected, actual)
    }

    @Test
    fun canParseWorkflow() {
        val expected = Workflow(
            entries = mapOf(
                Token.X to 787,
                Token.M to 2655,
                Token.A to 1222,
                Token.S to 2876,
            ),
        )
        val actual = parseWorkflow("{x=787,m=2655,a=1222,s=2876}")
        assertEquals(expected, actual)
    }
}
