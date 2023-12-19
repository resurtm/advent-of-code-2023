package com.resurtm.aoc2023.day19

fun launchDay19(testCase: String) {
    val input = readInput(testCase)
    println("Day 19, part 1: ${solvePart1(input)}")
}

internal fun solvePart1(input: Input): Long =
    input.workflows.fold(0L) { acc, it -> acc + traverseWorkflow(it, input.rules) }

private fun traverseWorkflow(workflow: Workflow, rules: Map<String, Rule>): Long {
    var curr = "in"

    while (curr !in arrayOf("A", "R")) {
        val rule = rules[curr] ?: throw Exception("A rule does not exist")

        conditions@ for (condition in rule.conditions) {
            when (condition) {
                is Condition.Full -> {
                    val val0 = workflow.entries[condition.token] ?: throw Exception("An entry value does not exist")
                    val val1 = condition.compVal

                    when (condition.compOp) {
                        CompOp.Less -> if (val0 < val1) {
                            curr = condition.nextRule
                            break@conditions
                        }

                        CompOp.Greater -> if (val0 > val1) {
                            curr = condition.nextRule
                            break@conditions
                        }
                    }
                }

                is Condition.Short -> {
                    curr = condition.nextRule
                    break@conditions
                }
            }
        }
    }

    return if (curr == "A") workflow.findSum() else 0
}
