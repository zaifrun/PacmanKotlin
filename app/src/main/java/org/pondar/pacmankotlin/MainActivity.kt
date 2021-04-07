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

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var myTimer: Timer = Timer()
    var counter: Int = 0
    val UP = 3
    val DOWN = 4
    val RIGHT = 1
    val LEFT = 2




    //reference to the game class.
    private var game: Game? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        //intialize the game view clas and game class


        startButton.setOnClickListener(this)
        pauseButton.setOnClickListener(this)
        game?.running = true
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }
        }, 0, 200000)
    }

    override fun onStop() {
        super.onStop()
        myTimer.cancel()
    }

    private fun timerMethod() {
        this.runOnUiThread(timerTick)
        this.runOnUiThread(newgame)
        this.runOnUiThread(setgame)
        this.runOnUiThread(sgW)
        this.runOnUiThread(gm)
        this.runOnUiThread(game!!.doCollisionCheck())
        this.runOnUiThread(game!!.initializeGoldcoins())
        this.runOnUiThread(game!!.newGame())




    }
    private val sgW = Runnable { game?.setGameView(gameView) }
    private val newgame = Runnable { game?.newGame() }
    private val setgame = Runnable { gameView.setGame(game) }
    private val gm = Runnable { game = Game(this, pointsView,MainActivity() ) }


    private val timerTick = Runnable {
        if (game?.running == true) {
            counter++
            textView.text = "Timer Value: {counter}"
            moveRight.setOnClickListener {
                game?.movedir(1)
            }
            moveLeft.setOnClickListener {
                game?.movedir(2)
            }
            moveUp.setOnClickListener {
                game?.movedir(3)
            }
            moveDown.setOnClickListener {
                game?.movedir(4)
            }
        }
    }



    override fun onClick(v: View) {
        if (v.id == R.id.startButton) {
            game?.running = true
        } else if (v.id == R.id.pauseButton) {
            game?.running = false
        } else if (v.id == R.id.action_newGame) {
            counter = 0
            game?.newGame()
            game?.running = false
            textView.text = "Timer Value: {counter}"
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
