package com.resurtm.aoc2023.day18

fun solvePart2(moves: List<Move>): Int {
    var borderArea = 0L
    var currPos = Pos()
    val lines = mutableListOf<Line>()

    for (move in moves) {
        val nextPos = getNextPos(currPos, move)
        val newLine = Line(currPos, nextPos)
        var addArea = newLine.length

        lines.forEach {
            if (newLine.intersects(it)) addArea--
        }

        borderArea += addArea
        currPos = nextPos
        lines.add(newLine)
    }

    println(borderArea)

    for (idx in 0..<lines.size - 2) {
        if (!isInside(lines[idx], lines[idx + 1], lines[idx + 2])) continue

        val points = mutableListOf<Pos>()
        repeat(3) {
            points.add(lines[idx + it].head)
            points.add(lines[idx + it].tail)
        }

        println("=============")
        println(findPointsMinMax(points))
    }

    return 0
}

private fun isInside(l0: Line, l1: Line, l2: Line): Boolean {
    val c0 = l0.head.col < l1.head.col && l2.tail.col < l1.head.col
    val c1 = l0.head.col > l1.head.col && l2.tail.col > l1.head.col
    val c2 = l0.head.row < l1.head.row && l2.tail.row < l1.head.row
    val c3 = l0.head.row > l1.head.row && l2.tail.row > l1.head.row
    return c0 || c1 || c2 || c3
}

private fun findPointsMinMax(points: List<Pos>): MinMax {
    val point = points.first()
    var min = point
    var max = point
    points.forEach {
        if (it.row < min.row) min = min.copy(row = it.row)
        if (it.col < min.col) min = min.copy(col = it.col)
        if (it.row > max.row) max = max.copy(row = it.row)
        if (it.col > max.col) max = max.copy(col = it.col)
    }
    return MinMax(min, max)
}

private data class Line(val head: Pos, val tail: Pos) {
    val start: Pos
    val end: Pos
    val length: Long

    init {
        val cmp = head.row < tail.row || head.col < tail.col
        this.start = if (cmp) head else tail
        this.end = if (cmp) tail else head

        this.length = if (start.row == end.row) (end.col - start.col + 1) else (end.row - start.row + 1)
    }

    fun intersects(line: Line): Boolean {
        val c1 = (this.start.row in line.start.row..line.end.row) &&
                (line.start.col in this.start.col..this.end.col)
        val c2 = (line.start.row in this.start.row..this.end.row) &&
                (this.start.col in line.start.col..line.end.col)
        return c1 || c2
    }

    override fun toString(): String = "start($start) & end($end) & len($length)"
}
