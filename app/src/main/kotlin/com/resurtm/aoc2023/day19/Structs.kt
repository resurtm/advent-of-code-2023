package com.resurtm.aoc2023.day19

internal data class Input(val rules: Map<String, Rule>, val workflows: List<Workflow>)

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

internal data class Workflow(val entries: Map<Token, Long>) {
    fun findSum(): Long = entries.values.reduce { acc, x -> acc + x }
}

internal data class Slice(
    val rule: String,
    val x: LongRange, val m: LongRange, val a: LongRange, val s: LongRange
) {
    fun isEmpty(): Boolean = x.isEmpty() && m.isEmpty() && a.isEmpty() && s.isEmpty()

    fun split(c: Condition.Full): Pair<Slice, Slice> = when (c.compOp) {
        CompOp.Less -> {
            val sl0 = Slice(
                rule = c.nextRule,
                x = if (c.token == Token.X) x.first..<c.compVal else x,
                m = if (c.token == Token.M) m.first..<c.compVal else m,
                a = if (c.token == Token.A) a.first..<c.compVal else a,
                s = if (c.token == Token.S) s.first..<c.compVal else s,
            )
            val sl1 = Slice(
                rule = "",
                x = if (c.token == Token.X) c.compVal..x.last else x,
                m = if (c.token == Token.M) c.compVal..m.last else m,
                a = if (c.token == Token.A) c.compVal..a.last else a,
                s = if (c.token == Token.S) c.compVal..s.last else s,
            )
            Pair(sl0, sl1)
        }

        CompOp.Greater -> {
            val sl0 = Slice(
                rule = c.nextRule,
                x = if (c.token == Token.X) c.compVal + 1..x.last else x,
                m = if (c.token == Token.M) c.compVal + 1..m.last else m,
                a = if (c.token == Token.A) c.compVal + 1..a.last else a,
                s = if (c.token == Token.S) c.compVal + 1..s.last else s,
            )
            val sl1 = Slice(
                rule = "",
                x = if (c.token == Token.X) x.first..c.compVal else x,
                m = if (c.token == Token.M) m.first..c.compVal else m,
                a = if (c.token == Token.A) a.first..c.compVal else a,
                s = if (c.token == Token.S) s.first..c.compVal else s,
            )
            Pair(sl0, sl1)
        }
    }

    fun findSum(): Long =
        (x.last - x.first + 1) * (m.last - m.first + 1) * (a.last - a.first + 1) * (s.last - s.first + 1)
}
