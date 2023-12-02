package com.resurtm.aoc2023

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val dayNum = parseArgs(args)
    if (!dayNum.first) {
        println("Unable to parse the provided day number")
        exitProcess(-1)
    }
    launchDayNum(dayNum.second)
}

fun parseArgs(args: Array<String>): Pair<Boolean, Int> {
    if (args.size != 1) {
        return Pair(false, 0)
    }
    return Pair(true, args.getOrElse(0) { "1" }.toInt())
}

fun launchDayNum(dayNum: Int) {
    when (dayNum) {
        1 -> launchDay01()
        else -> {
            println("Unable to launch the provided day number")
            exitProcess(-1)
        }
    }
}
