package com.resurtm.aoc2023.day19

internal fun readInput(testCase: String): Input {
    val reader =
        object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
            ?: throw Exception("Cannot read an input, probably an invalid input provided")

    val rules = mutableListOf<Rule>()
    val workflows = mutableListOf<Workflow>()

    while (true) {
        val rawLine = (reader.readLine() ?: break).trim()
        if (rawLine.isEmpty()) continue

        if (rawLine.firstOrNull() == '{')
            workflows.add(parseWorkflow(rawLine))
        else if (rawLine.firstOrNull() != '{')
            rules.add(parseRule(rawLine))
    }

    return Input(rules, workflows)
}

internal fun parseRule(line: String): Rule {
    if (line.trim().trim('{', '}').isEmpty())
        throw Exception("An invalid rule passed for parse")

    val parts0 = line.split('{').map { it.trim().trim('}') }
    val parts1 = parts0[1].split(',').map { it.trim() }

    return Rule(name = parts0[0], conditions = parts1.map { parseCondition(it) })
}

private fun parseCondition(rawCondition: String): Condition {
    val parts = rawCondition.split(CompOp.Less.value, CompOp.Greater.value, ':')
    return when (parts.size) {
        1 -> Condition.Short(nextRule = parts[0])

        3 -> Condition.Full(
            token = Token.fromValue(parts[0][0]),
            compOp = CompOp.fromRawCondition(rawCondition),
            compVal = parts[1].toLong(),
            nextRule = parts[2],
        )

        else -> throw Exception("An invalid rule passed for parse")
    }
}

internal fun parseWorkflow(line: String): Workflow {
    if (line.trim().trim('{', '}').isEmpty() || line.trim()[0] != '{')
        throw Exception("An invalid workflow passed for parse")

    val parts0 = line.trim('{', '}').split(',').map { it.trim() }
    val entries = parts0.map {
        val parts1 = it.split('=')
        Entry(token = Token.fromValue(parts1[0][0]), compVal = parts1[1].toLong())
    }

    return Workflow(entries = entries)
}
