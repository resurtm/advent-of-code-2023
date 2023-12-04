package com.resurtm.aoc2023

import com.resurtm.aoc2023.day01.launchDay01
import com.resurtm.aoc2023.day02.launchDay02
import com.resurtm.aoc2023.day03.launchDay03
import com.resurtm.aoc2023.day04.launchDay04
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    try {
        val taskInfo = parseArgs(args)
        launchSolution(taskInfo.first, taskInfo.second)
    } catch (ex: Exception) {
        println("Error: $ex")
        exitProcess(-1)
    }
}

fun parseArgs(args: Array<String>): Pair<Int, String> {
    if (args.size != 2) {
        throw Exception("Argument count must be two")
    }
    return Pair(args.getOrElse(0) { "1" }.toInt(), args.getOrElse(1) { "test" })
}

fun launchSolution(dayNum: Int, testCase: String) {
    val caseName = "/day%02d/%s.txt".format(dayNum, testCase)
    when (dayNum) {
        1 -> launchDay01(caseName)
        2 -> launchDay02(caseName)
        3 -> launchDay03(caseName)
        4 -> launchDay04(caseName)
        else -> {
            println("Unable to launch the provided day number")
            exitProcess(-1)
        }
    }
}
