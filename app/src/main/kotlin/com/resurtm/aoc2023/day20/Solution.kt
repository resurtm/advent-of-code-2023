package com.resurtm.aoc2023.day20

import java.util.ArrayDeque

fun launchDay20(testCase: String) {
    val mods = readInput(testCase)

    mods.forEach { (currName, currMod) ->
        currMod.next.filter { it != "output" }.forEach { nextName ->
            val nextMod = mods[nextName]
                ?: throw Exception("Cannot find a necessary module")
            if (nextMod.type == '&' && !nextMod.inps.contains(currName)) {
                nextMod.inps[currName] = false
            }
        }
    }

    val queue = ArrayDeque<QueueItem>()
    queue.add(QueueItem(
        mods.values.find { it.type == 'b' }
            ?: throw Exception("Cannot find a broadcaster")
    ))

    while (queue.isNotEmpty()) {
        val qItem = queue.pollFirst()
            ?: throw Exception("Queue should not be empty here")
        val nextMods = mods.values.filter { it.name in qItem.mod.next }

        val toAdd = mutableListOf<QueueItem>()
        if (qItem.mod.type == 'b') {
            toAdd.addAll(nextMods.map { QueueItem(it, qItem.high, qItem.mod.name) })
        } else if (qItem.mod.type == '%') {
            if (!qItem.high) {
                qItem.mod.on = !qItem.mod.on
                toAdd.addAll(nextMods.map { QueueItem(it, qItem.mod.on, qItem.mod.name) })
            }
        } else if (qItem.mod.type == '&') {
            qItem.mod.inps[qItem.prevName] = qItem.high
            val signal = !qItem.mod.inps.values.all { it }
            toAdd.addAll(nextMods.map { QueueItem(it, signal, qItem.mod.name) })
        } else throw Exception("Invalid queue item detected, cannot work on it")
        queue.addAll(toAdd)

        toAdd.forEach {
            println("${qItem.mod.name} - ${it.high} -> ${it.mod.name}")
        }
    }

    mods.values.forEach {
        println(it)
    }
}
