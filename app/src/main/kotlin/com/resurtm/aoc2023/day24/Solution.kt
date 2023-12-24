package com.resurtm.aoc2023.day24

fun launchDay24(testCase: String) {
    val neverTellMeTheOdds = NeverTellMeTheOdds.readInput(testCase)
    val part1 = neverTellMeTheOdds.solvePart1(
        if (testCase.contains("test")) 7L..27L
        else 200_000_000_000_000L..400_000_000_000_000L
    )
    println("Day 24, part 1: $part1")
}

private data class NeverTellMeTheOdds(
    val rays: List<Ray>
) {
    private val lines: List<Pair<Ray, Line>> = rays.map { Pair(it, Line.fromRay(it)) }

    fun solvePart1(interval: LongRange): Long {
        var res = 0L

        for (i in 0..lines.size - 2) for (j in i + 1..<lines.size) {
            val rayInter = Ray.findIntersection(lines[i].first, lines[j].first)
            if (!(rayInter.x > 0 && rayInter.y > 0))
                continue

            val lineInter = Line.findIntersection(lines[i].second, lines[j].second)
            if ((lineInter.x.toLong() in interval) && (lineInter.y.toLong() in interval))
                res++
        }

        return res
    }

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
                        Vec(parts1[0], parts1[1], parts1[2]),
                        Vec(parts2[0], parts2[1], parts2[2])
                    )
                )
            }

            return NeverTellMeTheOdds(rays)
        }
    }
}

private data class Ray(val p: Vec, val d: Vec) {
    companion object {
        internal fun findIntersection(r1: Ray, r2: Ray): VecF {
            val dx = (r2.p.x - r1.p.x).toDouble()
            val dy = (r2.p.y - r1.p.y).toDouble()
            val det = r2.d.x * r1.d.y - r2.d.y * r1.d.x
            val u = (dy * r2.d.x - dx * r2.d.y) / det
            val v = (dy * r1.d.x - dx * r1.d.y) / det
            return VecF(x = u, y = v, z = 0.0)
        }
    }
}

private data class Line(val a: Vec, val b: Vec) {
    companion object {
        internal fun findIntersection(l1: Line, l2: Line): VecF {
            val a1 = (l1.b.y - l1.a.y).toDouble()
            val b1 = (l1.a.x - l1.b.x).toDouble()
            val c1 = (a1 * l1.a.x + b1 * l1.a.y)

            val a2 = (l2.b.y - l2.a.y).toDouble()
            val b2 = (l2.a.x - l2.b.x).toDouble()
            val c2 = (a2 * l2.a.x + b2 * l2.a.y)

            val delta = (a1 * b2 - a2 * b1)
            return VecF(
                x = (b2 * c1 - b1 * c2) / delta,
                y = (a1 * c2 - a2 * c1) / delta,
                z = 0.0,
            )
        }

        internal fun fromRay(r: Ray): Line = Line(
            r.p.copy(),
            Vec(r.p.x + r.d.x, r.p.y + r.d.y, r.p.z + r.d.z)
        )
    }
}

private data class Vector<T>(val x: T, val y: T, val z: T)

private typealias Vec = Vector<Long>

private typealias VecF = Vector<Double>
