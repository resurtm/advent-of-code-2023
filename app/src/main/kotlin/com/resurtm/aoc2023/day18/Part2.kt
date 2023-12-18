package com.resurtm.aoc2023.day18

fun solvePart2(moves: List<Move>): Int {
    var area = 0L
    var curr = Pos()
    val lines = mutableListOf<Line>()

    for (move in moves) {
        val next = getNextPos(curr, move)

        val newLine = Line(curr, next)
        var newLineLen = newLine.length
        for (otherLine in lines) {
            if (newLine.intersects(otherLine)) newLineLen--
        }

        area += if (newLineLen > 0) newLineLen else 0
        curr = next
        lines.add(newLine)
    }

    println(area)
    return 0
}

private data class Line(private val p0: Pos, private val p1: Pos) {
    var start: Pos
    var end: Pos
    var length: Long

    init {
        val cmp = p0.row < p1.row || p0.col < p1.col
        this.start = if (cmp) p0 else p1
        this.end = if (cmp) p1 else p0

        this.length = if (start.row == end.row) (end.col - start.col) else (end.row - start.row)
        this.length++
    }

    fun intersects(line: Line): Boolean {
        val c1 = (this.start.row in line.start.row..line.end.row) &&
                (line.start.col in this.start.col..this.end.col)
        val c2 = (line.start.row in this.start.row..this.end.row) &&
                (this.start.col in line.start.col..line.end.col)
        return c1 || c2
    }

    override fun toString(): String = "$start - $end - $length"
}
