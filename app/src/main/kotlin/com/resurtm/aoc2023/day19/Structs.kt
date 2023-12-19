package com.resurtm.aoc2023.day19

internal data class Input(val rules: List<Rule>)

internal data class Rule(val name: String, val conditions: List<Condition>)

internal sealed class Condition(open val nextRule: String) {
    data class Short(override val nextRule: String) : Condition(nextRule)
    data class Full(
        val token: Token,
        val compOp: CompOp,
        val compVal: Long,
        override val nextRule: String
    ) : Condition(nextRule)
}

internal enum class Token(val value: Char) {
    X('x'), M('m'), A('a'), S('s');

    companion object {
        fun fromValue(ch: Char): Token = when (ch) {
            X.value -> X
            M.value -> M
            A.value -> A
            S.value -> S
            else -> throw Exception("An invalid token value passed for parse")
        }
    }
}

internal enum class CompOp(val value: Char) {
    Less('<'), Greater('>');

    companion object {
        fun fromRawCondition(rawCondition: String): CompOp {
            if (rawCondition.indexOf('<') != -1) return Less
            if (rawCondition.indexOf('>') != -1) return Greater
            throw Exception("An invalid condition passed for parse")
        }
    }
}

internal data class Workflow(val entries: List<Entry>)

internal data class Entry(val token: Token, val compVal: Long)
