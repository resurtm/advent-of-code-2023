package com.resurtm.aoc2023.day20

internal fun calcPart2(mods: Map<String, Mod>): Long {
    var res = PulseInfo()
    repeat(1000) {
        val re = pushButton(mods, findDeadMods(mods))
        res = PulseInfo(res.low + re.low, res.high + re.high)
    }
    return res.low * res.high
}
