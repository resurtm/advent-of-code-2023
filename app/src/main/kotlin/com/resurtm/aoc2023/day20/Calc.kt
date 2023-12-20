package com.resurtm.aoc2023.day20

import java.util.ArrayDeque

internal fun findDeadMods(mods: Map<String, Mod>): List<String> {
    val deadMods = mutableListOf<String>()
    mods.forEach { (currName, currMod) ->
        currMod.next.filter { it != "output" }.forEach { nextName ->
            val nextMod = mods[nextName]
            if (nextMod == null) {
                deadMods.add(nextName)
            } else if (!nextMod.inps.contains(currName)) {
                nextMod.inps[currName] = false
            }
        }
    }
    return deadMods
}

internal fun pushButton(
    mods: Map<String, Mod>,
    deadMods: List<String>,
    lookFor: String? = null,
): PulseInfo {
    val queue = ArrayDeque<QueueItem>()
    queue.add(QueueItem(
        mods.values.find { it.type == 'b' }
            ?: throw Exception("Cannot find a broadcaster")
    ))

    var lowP = 1L
    var highP = 0L
    var found = false

    while (queue.isNotEmpty()) {
        val qItem = queue.pollFirst()
            ?: throw Exception("Queue should not be empty here")

//        if (lookFor != null && qItem.mod.name == lookFor && qItem.high) {
//            found = true
//        }

        val add1 = if (qItem.mod.next.contains("output")) listOf(Mod('o', "output")) else emptyList()
        val add2 = deadMods.filter { it in qItem.mod.next }.map { Mod('d', "dead") }
        val nextMods = mods.values.filter { it.name in qItem.mod.next } + add1 + add2

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
            qItem.mod.on = signal
            toAdd.addAll(nextMods.map { QueueItem(it, signal, qItem.mod.name) })
            if (lookFor != null && qItem.mod.name == lookFor && qItem.mod.on) {
                // println(lookFor)
                // println(qItem.mod.inps.values.all { it })
                found = true
            }
        } else {
            throw Exception("Invalid queue item detected, cannot work on it")
        }

        queue.addAll(toAdd.filter { it.mod.type !in arrayOf('o', 'd') })
        toAdd.forEach {
            if (it.high) highP++
            else lowP++
        }
    }

    return PulseInfo(lowP, highP, found)
}
