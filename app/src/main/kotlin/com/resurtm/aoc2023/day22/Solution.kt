package com.resurtm.aoc2023.day22

import kotlin.math.max
import kotlin.math.min

fun launchDay22(testCase: String) {
    val space = Space()
    space.readBricks(testCase)
    space.applyGravity()
}

internal data class Pos(val x: Long, val y: Long, val z: Long)

internal data class Brick(val a: Pos, val b: Pos) {
    internal fun intersects(some: Brick): Boolean {
        return (this.a.x <= some.b.x && this.b.x >= some.a.x) &&
                (this.a.y <= some.b.y && this.b.y >= some.a.y)
    }
}

internal data class Space(var bricks: MutableList<Brick> = mutableListOf()) {
    internal fun applyGravity() {
        for (b0 in bricks.indices) {
            val br0 = bricks[b0]
            var maxZ = 1L

            for (b1 in 0..<b0) {
                val br1 = bricks[b1]
                if (br0.intersects(br1) && maxZ < br1.b.z + 1L)
                    maxZ = br1.b.z + 1L
            }

            val diffZ = br0.a.z - maxZ
            bricks[b0] = br0.copy(
                a = br0.a.copy(z = br0.a.z - diffZ),
                b = br0.b.copy(z = br0.b.z - diffZ)
            )
        }
    }

    internal fun readBricks(testCase: String) {
        val reader =
            object {}.javaClass.getResourceAsStream(testCase)?.bufferedReader()
                ?: throw Exception("Cannot read an input, probably it is invalid")

        val bricks = mutableListOf<Brick>()
        while (true) {
            val rawLine = reader.readLine()
                ?: break
            if (rawLine.trim().isEmpty())
                continue
            bricks.add(parseRawLine(rawLine.trim()))
        }
        this.bricks = bricks.sortedWith(compareBy { it.a.z }).toMutableList()
    }

    private fun parseRawLine(rawLine: String): Brick {
        val p0 = rawLine.split('~').map { it.trim() }
        val p1 = p0[0].split(',').map { it.trim().toLong() }
        val p2 = p0[1].split(',').map { it.trim().toLong() }

        val a = Pos(x = min(p1[0], p2[0]), y = min(p1[1], p2[1]), z = min(p1[2], p2[2]))
        val b = Pos(x = max(p1[0], p2[0]), y = max(p1[1], p2[1]), z = max(p1[2], p2[2]))
        val minZ = if (a.z <= b.z) a.z else b.z

        return Brick(a, b)
    }
}
