package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class game : AppCompatActivity() {

    private var lastBackPressTime: Long = 0
    private var currentPlayer = 1
    private val buttonStates = mutableMapOf<Int, Int>()
    private val buttontag =mutableMapOf<Int, Int>()
    private val player1Scores = mutableListOf<Int>()
    private val player2Scores = mutableListOf<Int>()
    private var bc = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)





        val intent = intent
        val player1Name = intent.getStringExtra("Player 1")
        val player2Name = intent.getStringExtra("Player 2")

        findViewById<TextView>(R.id.player1TextView).text = " $player1Name "
        findViewById<TextView>(R.id.player2TextView).text = " $player2Name "

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        val buttonCount = gridLayout.childCount

        for (i in 0 until buttonCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
            button.tag = 0
            button.isClickable = true
            buttonStates[i] = 0
            buttontag[i]=0
            button.setOnClickListener {
                incrementButton(button, gridLayout, i)
            }
        }

        updateBackgroundColor()


        val homeButton = findViewById<ImageButton>(R.id.imageButton4)
        homeButton.setOnClickListener {
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
        }
    }

    private fun returnWinnerAndFinish() {
        val winnerName = if (player1Scores.last() > player2Scores.last()) {
            intent.getStringExtra("Player 1") ?: "Player 1"
        } else {
            intent.getStringExtra("Player 2") ?: "Player 2"
        }

        val resultIntent = Intent().apply {
            putExtra("WinnerName", winnerName)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun incrementButton(button: Button, gridLayout: GridLayout, index: Int) {
        val currentCount = button.tag as Int
        if (bc < 2) {
            bc++
            var newcount= currentCount + 3
            if (currentCount== 0) {
                button.tag = newcount
                button.text = newcount.toString()
                buttonStates[index] = currentPlayer
                buttontag[index]= newcount.toInt()

                if (currentPlayer == 1) {
                    button.setBackgroundResource(R.drawable.player1)
                } else {
                    button.setBackgroundResource(R.drawable.player2)
                }
                currentPlayer = if (currentPlayer == 1) 2 else 1
                updateBackgroundColor()
                updateScores()
            }else{
                Toast.makeText(this, "You can only expand your own box.", Toast.LENGTH_SHORT).show()

                }

        };
        if (bc >= 2) {
            bc++
            var newCount = currentCount + 1

            if (buttonStates[index] == currentPlayer) {
                button.tag = newCount
                button.text = newCount.toString()
                buttonStates[index] = currentPlayer
                buttontag[index]=newCount

                if (currentPlayer == 1) {
                    button.setBackgroundResource(R.drawable.player1)
                } else {
                    button.setBackgroundResource(R.drawable.player2)
                }

                if (newCount == 4) {
                    button.tag = 0
                    button.text = ""
                    buttontag[index]=0
                    button.setBackgroundResource(R.drawable.roundstyle)
                    incrementAdjacentButtons(gridLayout, index)
                }
                currentPlayer = if (currentPlayer == 1) 2 else 1
                updateBackgroundColor()


                updateScores()
                checkGameEnd()
            } else {
                Toast.makeText(this, "You can only expand your own box.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun incrementAdjacentButtons(gridLayout: GridLayout, index: Int) {
        val rowCount = gridLayout.rowCount
        val columnCount = gridLayout.columnCount

        val row = index / columnCount
        val col = index % columnCount

        val adjacentIndices = listOf(
            index - columnCount,
            index + columnCount,
            index - 1,
            index + 1
        )

        for (adjIndex in adjacentIndices) {
            if (isValidIndex(adjIndex, rowCount, columnCount) && isAdjacent(row, col, adjIndex, columnCount)) {
                val adjButton = gridLayout.getChildAt(adjIndex) as Button
                val adjCount = adjButton.tag as Int

                adjButton.tag = adjCount + 1
                adjButton.text = adjButton.tag.toString()
                buttonStates[adjIndex] = currentPlayer
                adjButton.setBackgroundResource(if (currentPlayer == 1) R.drawable.player1 else R.drawable.player2)

                if (adjButton.tag as Int == 4) {
                    adjButton.setBackgroundResource(R.drawable.roundstyle)
                    adjButton.tag = 0
                    adjButton.text = ""
                    incrementAdjacentButtons(gridLayout, adjIndex)
                }
            }
        }
    }

    private fun isValidIndex(index: Int, rowCount: Int, columnCount: Int): Boolean {
        return index in 0 until (rowCount * columnCount)
    }

    private fun isAdjacent(row: Int, col: Int, adjIndex: Int, columnCount: Int): Boolean {
        val adjRow = adjIndex / columnCount
        val adjCol = adjIndex % columnCount

        return when {
            row == adjRow -> Math.abs(col - adjCol) == 1
            col == adjCol -> Math.abs(row - adjRow) == 1
            else -> false
        }
    }

    private fun updateBackgroundColor() {
        val rootView = findViewById<ConstraintLayout>(R.id.game)
        val color = if (currentPlayer == 1) {
            ContextCompat.getColor(this, R.color.player1backgroundcolour)
        } else {
            ContextCompat.getColor(this, R.color.player2backgroundcolour)
        }
        rootView.setBackgroundColor(color)
    }

    private fun updateScores() {
        var player1Score = 0
        var player2Score = 0

        for (i in buttonStates.keys) {
            if (buttonStates[i]== 1) {
                player1Score++




            } else if (buttonStates[i] == 2) {
                player2Score++


            }
        }

        player1Scores.add(player1Score)
        player2Scores.add(player2Score)

        findViewById<TextView>(R.id.player1ScoreTextView).text = player1Score.toString()
        findViewById<TextView>(R.id.player2ScoreTextView).text = player2Score.toString()
    }

    private fun checkGameEnd() {
        if (gameEnd()) {
            displayScores()
        }
    }

    private fun gameEnd(): Boolean {
        for (i in 1 until player1Scores.size) {
            if (player1Scores[i] == 0 || player2Scores[i] == 0) {
                return true
            }
        }
        return false
    }

    private fun displayScores() {
        val dialog = Dialog(this@game)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.winner)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)



        val winnerNameTextView = dialog.findViewById<TextView>(R.id.winner)
        val okButton = dialog.findViewById<Button>(R.id.ok)

        val winnerName = if (player1Scores.last() > player2Scores.last()) {
            intent.getStringExtra("Player 1") ?: "Player 1"
        } else {
            intent.getStringExtra("Player 2") ?: "Player 2"
        }

        winnerNameTextView.text = winnerName

        okButton.setOnClickListener {
            dialog.dismiss()
            returnWinnerAndFinish()
        }

        dialog.show()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < 2000) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            lastBackPressTime = currentTime
        }
    }
}




