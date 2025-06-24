package com.example.a2048

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout

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
