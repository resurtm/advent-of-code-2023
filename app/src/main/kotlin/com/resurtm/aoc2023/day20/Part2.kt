package com.resurtm.aoc2023.day20

internal fun calcPart2(mods: Map<String, Mod>): Long {
    var presses = 0L
    while (true) {
        println(presses)
        presses++
        if (pushButton(mods, findDeadMods(mods), lookForDead = true).foundDead)
            break
    }
    return presses
}
