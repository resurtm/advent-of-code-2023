package com.resurtm.aoc2023.day24

fun launchDay24(testCase: String) {
    println(NeverTellMeTheOdds.readInput(testCase))
}

private data class NeverTellMeTheOdds(
    val rays: List<Ray>
) {
    companion object {
        internal fun readInput(testCase: String): NeverTellMeTheOdds {
            val reader =
                object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
                    ?: throw Exception("Cannot read an input, probably it is invalid")

            val rays = mutableListOf<Ray>()
            while (true) {
                val rawLine = reader.readLine()
                    ?: break
                if (rawLine.trim().isEmpty())
                    continue

                val parts0 = rawLine.split("@")
                val parts1 = parts0[0].split(',').map { it.trim().toLong() }
                val parts2 = parts0[1].split(',').map { it.trim().toLong() }

                rays.add(
                    Ray(
                        Pos(parts1[0], parts1[1], parts1[2]),
                        Pos(parts2[0], parts2[1], parts2[2])
                    )
                )
            }

            return NeverTellMeTheOdds(rays)
        }
    }
}

private data class Ray(val p: Pos, val d: Pos)

private data class Line(val a: Pos, val b: Pos)

private data class Position<T>(val x: T, val y: T, val z: T)

private typealias Pos = Position<Long>
