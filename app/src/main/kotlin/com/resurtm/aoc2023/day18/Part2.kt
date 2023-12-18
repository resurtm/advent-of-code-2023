package com.resurtm.aoc2023.day18

fun solvePart2(moves: List<Move>): Int {
    var totalArea = 0L
    var currPos = Pos()
    val lines = mutableListOf<Line>()

    for (move in moves) {
        val nextPos = getNextPos(currPos, move)
        val newLine = Line(currPos, nextPos)
        var addArea = newLine.length

        lines.forEach {
            if (newLine.intersects(it)) addArea--
        }

        totalArea += addArea
        currPos = nextPos
        lines.add(newLine)
    }

    println(totalArea)
    return 0
}

private data class Line(private val p0: Pos, private val p1: Pos) {
    val start: Pos
    val end: Pos
    val length: Long

    init {
        val cmp = p0.row < p1.row || p0.col < p1.col
        this.start = if (cmp) p0 else p1
        this.end = if (cmp) p1 else p0

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
