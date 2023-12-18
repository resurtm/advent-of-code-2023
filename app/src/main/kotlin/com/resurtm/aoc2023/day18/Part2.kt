package com.resurtm.aoc2023.day18

fun solvePart2(moves: List<Move>): Int {
    // part 1
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

    // part 2
    var rectArea = 0L

    for (idx in 0..<lines.size - 2) {
        val isCw = areLinesCw(lines[idx], lines[idx + 1], lines[idx + 2])
        val isCcw = areLinesCcw(lines[idx], lines[idx + 1], lines[idx + 2])
        if (!isCw && !isCcw) continue

        val points = mutableListOf<Pos>()
        repeat(3) {
            points.add(lines[idx + it].head)
            points.add(lines[idx + it].tail)
        }
        val minMax = findPointsMinMax(points)
        // if (countLinesInMinMax(minMax, lines) !in arrayOf(4, 5)) continue

        println("=============")
        println(minMax)
        println(isCw)
        println(isCcw)
        println(minMax)
        println(countLinesInMinMax(minMax, lines))

        val addArea = (minMax.max.row - minMax.min.row) * (minMax.max.col - minMax.min.col)
        rectArea += addArea
    }

    println(rectArea)

    return 0
}

private fun areLinesCw(l0: Line, l1: Line, l2: Line): Boolean {
    val c0 = false
    //val c0 = l0.head.col < l1.head.col && l2.tail.col < l1.head.col
    val c1 = l0.head.col > l1.head.col && l2.tail.col > l1.head.col
    //val c2 = l0.head.row < l1.head.row && l2.tail.row < l1.head.row
    val c2 = false
    val c3 = l0.head.row > l1.head.row && l2.tail.row > l1.head.row
    return c0 || c1 || c2 || c3
}

private fun areLinesCcw(l0: Line, l1: Line, l2: Line): Boolean {
    val c0 = l0.head.col < l1.head.col && l2.tail.col < l1.head.col
    //val c1 = l0.head.col > l1.head.col && l2.tail.col > l1.head.col
    val c1 = false
    val c2 = l0.head.row < l1.head.row && l2.tail.row < l1.head.row
    //val c3 = l0.head.row > l1.head.row && l2.tail.row > l1.head.row
    val c3 = false
    return c0 || c1 || c2 || c3
}

private fun countLinesInMinMax(minMax: MinMax, lines: List<Line>): Int {
    val line0 = Line(Pos(minMax.min.row, minMax.min.col), Pos(minMax.max.row, minMax.min.col))
    val line1 = Line(Pos(minMax.max.row, minMax.max.col), Pos(minMax.max.row, minMax.min.col))
    val line2 = Line(Pos(minMax.max.row, minMax.max.col), Pos(minMax.min.row, minMax.max.col))
    val line3 = Line(Pos(minMax.min.row, minMax.min.col), Pos(minMax.min.row, minMax.max.col))

    var result = 0
    for (line in lines) {
        if (line.start.isInside(minMax) || line.end.isInside(minMax)) {
            result++; continue
        }
        if (line.intersects(line0) || line.intersects(line1) || line.intersects(line2) || line.intersects(line3)) {
            result++; continue
        }
    }
    return result
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
