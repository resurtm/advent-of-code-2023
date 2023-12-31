package com.resurtm.aoc2023.day24

import kotlin.math.round

/**
 * See the README.md file for more details on this one.
 */
fun launchDay24(testCase: String) {
    val neverTellMeTheOdds = NeverTellMeTheOdds.readInput(testCase)

    val part1 = neverTellMeTheOdds.solvePart1(
        if (testCase.contains("test")) 7L..27L
        else 200_000_000_000_000L..400_000_000_000_000L
    )
    println("Day 24, part 1: $part1")

    // val part2 = neverTellMeTheOdds.solvePart2Slow()
    // println("Day 24, part 2: $part2")

    val part2 = neverTellMeTheOdds.solvePart2()
    println("Day 24, part 2: $part2")
}

private data class NeverTellMeTheOdds(
    val rays: List<Ray>
) {
    private val pairs: List<Pair<Ray, Line>> = rays.map { Pair(it, Line.fromRay(it)) }

    /**
     * See the README.md file for more details on this one.
     */
    fun solvePart2(): Long {
        // step 1 -- find velocity
        val velsX = mutableMapOf<Long, MutableList<Long>>()
        pairs.forEach {
            val v = velsX[it.first.dir.x]
            if (v == null) velsX[it.first.dir.x] = mutableListOf(it.first.start.x)
            else v.add(it.first.start.x)
        }
        val velsY = mutableMapOf<Long, MutableList<Long>>()
        pairs.forEach {
            val v = velsY[it.first.dir.y]
            if (v == null) velsY[it.first.dir.y] = mutableListOf(it.first.start.y)
            else v.add(it.first.start.y)
        }
        val velsZ = mutableMapOf<Long, MutableList<Long>>()
        pairs.forEach {
            val v = velsZ[it.first.dir.z]
            if (v == null) velsZ[it.first.dir.z] = mutableListOf(it.first.start.z)
            else v.add(it.first.start.z)
        }
        val vel = Vec(findVelocity(velsX), findVelocity(velsY), findVelocity(velsZ))

        // step 2 -- find position
        val results = mutableMapOf<Long, Long>()
        for (i in pairs.indices) for (j in i + 1..<pairs.size) {
            val rayA = pairs[i].first
            val rayB = pairs[j].first

            val ma = (rayA.dir.y - vel.y).toDouble() / (rayA.dir.x - vel.x).toDouble()
            val mb = (rayB.dir.y - vel.y).toDouble() / (rayB.dir.x - vel.x).toDouble()

            val ca = rayA.start.y - (ma * rayA.start.x)
            val cb = rayB.start.y - (mb * rayB.start.x)

            val rpx = (cb - ca) / (ma - mb)
            val rpy = (ma * rpx + ca)

            val time = round((rpx - rayA.start.x) / (rayA.dir.x - vel.x).toDouble())
            val rpz = rayA.start.z + (rayA.dir.z - vel.z) * time

            val result = (rpx + rpy + rpz).toLong()
            val ex = results[result]
            if (ex == null) results[result] = 1
            else results[result] = ex + 1
        }

        return results.entries.sortedBy { it.value }.last().key
    }

    private fun findVelocity(velocities: Map<Long, List<Long>>): Long {
        var possible = mutableListOf<Long>()
        for (possib in -1000L..1000L) possible.add(possib)

        for ((vel, pos) in velocities) {
            if (pos.size < 2) continue

            val newPossible = mutableListOf<Long>()
            for (possib in possible) {
                if ((possib - vel) !== 0L && (pos[0] - pos[1]) % (possib - vel) === 0L) {
                    newPossible.add(possib)
                }
            }
            possible = newPossible
        }

        return possible.firstOrNull() ?: throw Exception("Unable to find a possible velocity")
    }

    fun solvePart2Slow(): Long {
        val posRange = 0L..25L
        val velRange = -5L..5L
        val timeLimit = 50L
        var result: Ray? = null

        main@ for (ix in posRange) for (iy in posRange) for (iz in posRange) {
            // println("$ix,$iy,$iz")

            for (vx in velRange) for (vy in velRange) for (vz in velRange) {
                val pos = Vec(ix, iy, iz)
                val vel = Vec(vx, vy, vz)
                val ray = Ray(pos, vel, pos)

                reset()
                for (t in 0..timeLimit) {
                    for (pair in pairs) {
                        if (pair.first.curr == ray.curr)
                            pair.first.hit = true
                    }
                    ray.curr = Vec(ray.curr.x + ray.dir.x, ray.curr.y + ray.dir.y, ray.curr.z + ray.dir.z)
                    advance()
                }

                var found = true
                for (pair in pairs) {
                    if (!pair.first.hit) {
                        found = false
                        break
                    }
                }
                if (found) result = ray
            }
        }

        println(result)
        return if (result == null) throw Exception("Unable to find a result")
        else result.start.x + result.start.z + result.start.z
    }

    private fun reset() {
        for (pair in pairs) {
            pair.first.curr = pair.first.start
            pair.first.hit = false
        }
    }

    private fun advance() {
        for (pair in pairs) {
            val pos = pair.first.curr
            val vel = pair.first.dir
            pair.first.curr = Vec(pos.x + vel.x, pos.y + vel.y, pos.z + vel.z)
        }
    }

    fun solvePart1(interval: LongRange): Long {
        var res = 0L

        for (i in 0..pairs.size - 2) for (j in i + 1..<pairs.size) {
            if (!Ray.hasInter2D(pairs[i].first, pairs[j].first))
                continue

            val inter = Line.findInter2D(pairs[i].second, pairs[j].second)
            if ((inter.x.toLong() in interval) && (inter.y.toLong() in interval))
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
                        Vec(parts2[0], parts2[1], parts2[2]),
                        Vec(parts1[0], parts1[1], parts1[2]),
                    )
                )
            }

            return NeverTellMeTheOdds(rays)
        }
    }
}

private data class Ray(
    val start: Vec,
    val dir: Vec,
    var curr: Vec,
    var hit: Boolean = false,
) {
    companion object {
        internal fun hasInter2D(r1: Ray, r2: Ray): Boolean {
            val dx = (r2.start.x - r1.start.x).toDouble()
            val dy = (r2.start.y - r1.start.y).toDouble()
            val det = r2.dir.x * r1.dir.y - r2.dir.y * r1.dir.x
            val u = (dy * r2.dir.x - dx * r2.dir.y) / det
            val v = (dy * r1.dir.x - dx * r1.dir.y) / det
            return u > 0 && v > 0
        }
    }
}

private data class Line(val a: Vec, val b: Vec) {
    companion object {
        internal fun findInter2D(l1: Line, l2: Line): VecF {
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
            r.start.copy(),
            Vec(r.start.x + r.dir.x, r.start.y + r.dir.y, r.start.z + r.dir.z)
        )
    }
}

private data class Vector<T>(val x: T, val y: T, val z: T)

private typealias Vec = Vector<Long>

private typealias VecF = Vector<Double>
