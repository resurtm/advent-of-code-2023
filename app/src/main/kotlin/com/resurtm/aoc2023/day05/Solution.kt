package com.resurtm.aoc2023.day05

fun launchDay05(testCase: String) {
    val environment = readEnvironment(testCase)
    println("$environment")
}

private fun readEnvironment(testCase: String): Environment {
    val rawReader = object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
    val reader = rawReader ?: throw Exception("Cannot read the input")

    val seeds = mutableListOf<Long>()
    val transformations = mutableListOf<List<Transformation>>()
    var transformation = mutableListOf<Transformation>()
    var readMap = false

    while (true) {
        val line = reader.readLine() ?: break
        if (readMap) {
            if (line.isEmpty()) {
                transformations.add(transformation)
                transformation = mutableListOf()
                readMap = false
                continue
            }
            val raw = line.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toLong() }
            transformation.add(Transformation(dst = raw[0], src = raw[1], len = raw[2]))
        }
        if (line.contains("seeds: ")) {
            seeds.addAll(line.split(":")[1].trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                .map { it.toLong() })
            continue
        }
        if (line.contains(" map:")) {
            readMap = true
            continue
        }
    }

    return Environment(seeds = seeds, transformations = transformations)
}

private data class Environment(val seeds: List<Long>, val transformations: List<List<Transformation>>)

private data class Transformation(val dst: Long, val src: Long, val len: Long)
