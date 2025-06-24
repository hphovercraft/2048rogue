package com.example.a2048

import kotlin.random.Random

enum class Direction { UP, DOWN, LEFT, RIGHT }

class Game2048 {
    val board = Array(4) { IntArray(4) }

    fun addRandomTile() {
        val empties = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                if (board[r][c] == 0) empties.add(r to c)
            }
        }
        if (empties.isNotEmpty()) {
            val (r, c) = empties.random()
            board[r][c] = if (Random.nextFloat() < 0.9f) 2 else 4
        }
    }

    fun move(dir: Direction): Boolean {
        var moved = false
        repeat(when (dir) { Direction.LEFT, Direction.RIGHT -> 1; else -> 4 }) {
            for (i in 0 until 4) {
                val line = when (dir) {
                    Direction.LEFT -> board[i]
                    Direction.RIGHT -> board[i].reversedArray()
                    Direction.UP -> IntArray(4) { board[it][i] }
                    Direction.DOWN -> IntArray(4) { board[3 - it][i] }
                }
                val merged = compress(line)
                if (!line.contentEquals(merged)) moved = true
                when (dir) {
                    Direction.LEFT -> board[i] = merged
                    Direction.RIGHT -> board[i] = merged.reversedArray()
                    Direction.UP -> for (j in 0 until 4) board[j][i] = merged[j]
                    Direction.DOWN -> for (j in 0 until 4) board[3 - j][i] = merged[j]
                }
            }
        }
        return moved
    }

    private fun compress(line: IntArray): IntArray {
        val list = line.filter { it != 0 }.toMutableList()
        var i = 0
        while (i < list.size - 1) {
            if (list[i] == list[i + 1]) {
                list[i] *= 2
                list.removeAt(i + 1)
            }
            i++
        }
        while (list.size < 4) list.add(0)
        return list.toIntArray()
    }
}
