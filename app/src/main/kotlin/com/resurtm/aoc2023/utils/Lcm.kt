package com.resurtm.aoc2023.utils

fun findListLCM(items: List<Long>): Long {
    var result = items[0]
    for (i in 1 until items.size) {
        result = findItemLCM(result, items[i])
    }
    return result
}

fun findItemLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}
