package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var moveTimer: Timer = Timer()
    private var gameTimer: Timer = Timer()
    var counter: Int = 0

    //reference to the game class.
    private var game: Game? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        game = Game(this,pointsView, timerView)

        //initialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveRight.setOnClickListener {
            game?.movePacmanRight(10)
        }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(10)
        }
        moveUp.setOnClickListener {
            game?.movePacmanUp(10)
        }
        moveDown.setOnClickListener {
            game?.movePacmanDown(10)
        }

        game?.running = true // Should game be running?

        // Timer for movement
        moveTimer.schedule((object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }), 0, 100)

        // Timer for game period
        gameTimer.schedule((object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }), 0, 1000)
    }

    private fun timerMethod() {

        this.runOnUiThread(timerTick)
    }

    private fun gametimerMethod() {
        this.runOnUiThread(gameTimerTick)
    }

    private val timerTick = Runnable {
        if (game!!.running) {
            game?.movePacman(10)
        }
    }

    private val gameTimerTick = Runnable {
        if (game!!.running) {
            game?.timer()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
