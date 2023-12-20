package com.resurtm.aoc2023.day20

import com.resurtm.aoc2023.utils.findListLCM

internal fun calcPart2(origMods: Map<String, Mod>): Long {
    // stage 1
    val tempMods0 = copyMods(origMods)
    findDeadMods(tempMods0)

    val conns0 = tempMods0.values.filter { it.next.contains("rx") }
    val conns1 = mutableListOf<Mod>()
    conns0.forEach { item ->
        conns1.addAll(tempMods0.values.filter { it.next.contains(item.name) })
    }

    // stage 2
    val nums = mutableListOf<Long>()
    conns1.forEach {
        // println(it.name)
        nums.add(lookFor(origMods, it.name))
    }
    // println(nums)
    return findListLCM(nums)
}

private fun lookFor(origMods: Map<String, Mod>, modName: String): Long {
    val mods = copyMods(origMods)
    var presses = 0L
    while (true) {
        val res = pushButton(mods, findDeadMods(mods), lookFor = modName)
        presses++
        if (res.found)
            break
    }
    return presses
}

internal fun copyMods(mods: Map<String, Mod>): Map<String, Mod> {
    val result = mutableMapOf<String, Mod>()
    mods.forEach { (k, v) -> result[k] = Mod(v.type, v.name, v.next.map { it }) }
    return result
}
