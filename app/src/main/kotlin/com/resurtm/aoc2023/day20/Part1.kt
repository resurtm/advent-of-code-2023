package com.resurtm.aoc2023.day20

internal fun calcPart1(mods: Map<String, Mod>): Long {
    var result = PulseInfo()
    repeat(1000) {
        val pulseInfo = pushButton(mods, findDeadMods(mods))
        result = PulseInfo(result.low + pulseInfo.low, result.high + pulseInfo.high)
    }
    return result.low * result.high
}
