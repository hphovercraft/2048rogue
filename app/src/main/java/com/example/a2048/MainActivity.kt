package com.example.a2048

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var grid: GridLayout
    private lateinit var detector: GestureDetector
    private val game = Game2048()
    private val cells = Array(4) { arrayOfNulls<TextView>(4) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        grid = findViewById(R.id.grid)
        detector = GestureDetector(this, this)
        setupGrid()
        game.addRandomTile()
        game.addRandomTile()
        updateUI()
    }

    private fun setupGrid() {
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                val tv = TextView(this).apply {
                    textSize = 32f
                    setPadding(16, 16, 16, 16)
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                }
                grid.addView(tv)
                cells[r][c] = tv
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event)
    }

    private fun updateUI() {
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                val value = game.board[r][c]
                val cell = cells[r][c]
                cell?.text = if (value == 0) "" else value.toString()
            }
        }
    }

    private fun move(dir: Direction) {
        if (game.move(dir)) {
            game.addRandomTile()
            updateUI()
        }
    }

    override fun onDown(e: MotionEvent): Boolean = true
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onLongPress(e: MotionEvent) {}

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y
        if (kotlin.math.abs(diffX) > kotlin.math.abs(diffY)) {
            if (diffX > 0) move(Direction.RIGHT) else move(Direction.LEFT)
        } else {
            if (diffY > 0) move(Direction.DOWN) else move(Direction.UP)
        }
        return true
    }
}

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
