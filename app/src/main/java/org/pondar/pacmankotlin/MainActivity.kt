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

    private var game: Game? = null
    private var myTimer: Timer = Timer()
    var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        //intialize the game view clas and game class
        game = Game(this, pointsView) //Det var her fejlen lå. Jeg havde lagt game variablen her under de 2 efterfølgende variabler som blev brugt før variablen eller klassen blev instantieret.
        game?.setGameView(gameView)
        game?.newGame()
        gameView.setGame(game)
        //startButton.setOnClickListener
        //stopButton.setOnClickListener


        //make a new timer
        game!!.running = true //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 200) //0 indicates we start now, 200
        //is the number of miliseconds between each call
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

    override fun onStop() {
        super.onStop()
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel()
    }

    private fun timerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //we could do updates here TO GAME LOGIC,
        // but not updates TO ACTUAL UI

        // gameView.move(20)  // BIG NO NO TO DO THIS - WILL CRASH ON OLDER DEVICES!!!!


        //We call the method that will work with the UI
        //through the runOnUiThread method.

        this.runOnUiThread(timerTick)

    }


    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game!!.running) {
            counter++
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            textView.text = getString(R.string.timerValue, counter)


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
        } else if (id == R.id.startButton) {
            game!!.running = true
        } else if (id == R.id.stopButton) {
            game!!.running = false
        } else if (id == R.id.action_newGame) {
            counter = 0
            game!!.newGame() //you should call the newGame method instead of this
            game!!.running = false
            textView.text = getString(R.string.timerValue, counter)}
            return super.onOptionsItemSelected(item)}

}



