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

    println("Debug: $borderArea")

    // part 2
    val rects = mutableListOf<Rect>()

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
        val linesInMinMax = countLinesInMinMax(minMax, lines)

        rects.add(Rect(minMax, isCw, isCcw, linesInMinMax, lines = listOf(lines[idx], lines[idx + 1], lines[idx + 2])))
    }

    var fillArea = 0L

    main@ for (i in 0..<rects.size) {
        val rect0 = rects[i]
        if (rect0.checked || rect0.isCw) continue
        rect0.checked = true

        var addFillArea = rect0.area

        for (j in 0..<rects.size) {
            val rect1 = rects[j]
            if (rect1.checked) continue
            rect1.checked = true

            if (rect1.isCcw) {
                val localArea = rect1.area - findCommonArea(rect0, rect1)
                addFillArea += localArea
                println(localArea)
            } else if (rect1.isCw) {
                val localArea = -findCommonArea(rect0, rect1, small = true)
                addFillArea += localArea
                println(localArea)
            }
            // break@main
        }

        fillArea += addFillArea
    }

    println("Result: $fillArea")
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

private fun countLinesInMinMax(minMax: MinMax, lines: List<Line>): Long {
    val line0 = Line(Pos(minMax.min.row, minMax.min.col), Pos(minMax.max.row, minMax.min.col))
    val line1 = Line(Pos(minMax.max.row, minMax.max.col), Pos(minMax.max.row, minMax.min.col))
    val line2 = Line(Pos(minMax.max.row, minMax.max.col), Pos(minMax.min.row, minMax.max.col))
    val line3 = Line(Pos(minMax.min.row, minMax.min.col), Pos(minMax.min.row, minMax.max.col))

    var result = 0L
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
