package com.resurtm.aoc2023

import com.resurtm.aoc2023.day01.launchDay01
import com.resurtm.aoc2023.day02.launchDay02
import com.resurtm.aoc2023.day03.launchDay03
import com.resurtm.aoc2023.day04.launchDay04
import com.resurtm.aoc2023.day05.launchDay05
import com.resurtm.aoc2023.day06.launchDay06
import com.resurtm.aoc2023.day07.launchDay07
import com.resurtm.aoc2023.day08.launchDay08
import com.resurtm.aoc2023.day09.launchDay09
import com.resurtm.aoc2023.day10.launchDay10
import com.resurtm.aoc2023.day11.launchDay11
import com.resurtm.aoc2023.day12.launchDay12
import com.resurtm.aoc2023.day13.launchDay13
import com.resurtm.aoc2023.day14.launchDay14
import com.resurtm.aoc2023.day15.launchDay15
import com.resurtm.aoc2023.day16.launchDay16
import com.resurtm.aoc2023.day17.launchDay17
import com.resurtm.aoc2023.day18.launchDay18
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val taskInfo = parseArgs(args)
    launchSolution(taskInfo.first, taskInfo.second)
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
        5 -> launchDay05(caseName)
        6 -> launchDay06(caseName)
        7 -> launchDay07(caseName)
        8 -> launchDay08(caseName)
        9 -> launchDay09(caseName)
        10 -> launchDay10(caseName)
        11 -> launchDay11(caseName)
        12 -> launchDay12(caseName)
        13 -> launchDay13(caseName)
        14 -> launchDay14(caseName)
        15 -> launchDay15(caseName)
        16 -> launchDay16(caseName)
        17 -> launchDay17(caseName)
        18 -> launchDay18(caseName)
        else -> {
            println("Unable to launch the provided day number")
            exitProcess(-1)
        }
    }
}
