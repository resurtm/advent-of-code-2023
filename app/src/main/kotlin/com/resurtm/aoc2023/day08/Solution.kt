package com.resurtm.aoc2023.day08

fun launchDay08(testCase: String) {
    val ex = Exception("Cannot read the input")
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw ex

    val path = reader.readLine() ?: throw ex
    val points = mutableMapOf<String, Pair<String, String>>()

    while (true) {
        val line = reader.readLine() ?: break
        if (line.indexOfAny(charArrayOf('=', ',')) == -1) continue

        val temp1 = line.split('=')
        val temp2 = temp1[1].split(',')

        val key = temp1[0].trim()
        val value = Pair(temp2[0].trim().trim('('), temp2[1].trim().trim(')'))
        points[key] = value
    }

    for (dir in path) {
        println(dir)
    }
    println(points)
}
