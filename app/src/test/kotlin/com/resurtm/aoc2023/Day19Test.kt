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
            entries = listOf(
                Entry(token = Token.X, compVal = 787),
                Entry(token = Token.M, compVal = 2655),
                Entry(token = Token.A, compVal = 1222),
                Entry(token = Token.S, compVal = 2876),
            ),
        )
        val actual = parseWorkflow("{x=787,m=2655,a=1222,s=2876}")
        assertEquals(expected, actual)
    }
}
