package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.util.ArrayList
import kotlin.math.sqrt
import java.util.*

/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView, view2: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0
        private var timerView: TextView = view2

        var time: Int = 120

        var levelUp: Int = 0

        //bitmap of the pacman
        var pacBitmap: Bitmap
        var coinBitmap: Bitmap
        var enemyBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0

        val RIGHT = 1
        val UP = 2
        val LEFT = 3
        val DOWN = 4

        var direction = RIGHT

        var running = false


        //did we initialize the coins and enemies?
        var objectsInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()

        // The list of enemies
        var enemies = ArrayList<Enemy>()

        //a reference to the gameview
        private var gameView: GameView? = null
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen

    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)

    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    fun pause(){
        if(time > 0){
            running = !running
        }
    }

    //TODO initialize goldcoins also here
    fun initializeObjects()
    {
        coins.clear()

        for (x in 0 until 10) {
            var randomX = Random().nextInt(w)
            var randomY = Random().nextInt(h)
            coins.add(GoldCoin(randomX.toFloat(), randomY.toFloat()))
        }

        enemies.clear()

        for (x in 0 until levelUp) {
            var randomX = Random().nextInt(w)
            var randomY = Random().nextInt(h)
            enemies.add(Enemy(randomX.toFloat(), randomY.toFloat()))
        }

        objectsInitialized = true
    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        objectsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen
        direction = 0
        time = 360
        timerView.text = "time left: $time"
        running = true
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacman(pixels: Int) {
        if (direction == RIGHT && pacx + pixels + pacBitmap.width < w) {
            // move Right
            pacx = pacx + pixels
        }
        else if (direction == UP && pacy - pixels + pacBitmap.height > 0) {
            // move Up
            pacy = pacy - pixels
        }
        else if (direction == LEFT && pacx - pixels + pacBitmap.width > 0) {
            // move Left
            pacx = pacx - pixels
        }
        else if (direction == DOWN && pacy + pixels + pacBitmap.height < h) {
            // move Down
            pacy = pacy + pixels
        }

        // Do Enemy Movement here
        for (enemy in enemies) {
            if (enemy.direction == RIGHT && enemy.posX + pixels + enemyBitmap.width < w) {
                // move enemy Right
                enemy.posX = enemy.posX + (pixels/2 + levelUp*2)
            }
            else if (enemy.direction == UP && enemy.posY + pixels + enemyBitmap.height < h) {
                // move enemy Up
                enemy.posY = enemy.posY + (pixels/2 + levelUp*2)
            }
            else if (enemy.direction == LEFT && enemy.posX + pixels + enemyBitmap.width > w) {
                // move enemy Left
                enemy.posX = enemy.posX - (pixels/2 + levelUp*2)
            }
            else if (enemy.direction == DOWN && enemy.posY + pixels + enemyBitmap.height > h) {
                // move enemy Down
                enemy.posY = enemy.posY - (pixels/2 + levelUp*2)
            }
        }

        doCollisionCheck()
        gameView!!.invalidate()
    }

    fun movePacmanRight(pixels: Int) {
        // Is Pacman still within boundaries of screen?
        direction = 1
    }

    fun movePacmanLeft(pixels: Int) {
        // Is Pacman still within boundaries of screen?
        direction = 3
    }

    fun movePacmanUp(pixels: Int) {
        // Is Pacman still within boundaries of screen?
        direction = 2
    }

    fun movePacmanDown(pixels: Int) {
        // Is Pacman still within boundaries of screen?
        direction = 4
    }

    fun distance(x1:Float, y1:Float, x2:Float, y2:Float) : Float {
        // Calculate the distance and then return it
        return sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)))
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        for (coin in coins) {
            if (!coin.acquired) {
                if (distance(coin.posX, coin.posY, pacx.toFloat(), pacy.toFloat()) < 100) {
                    coin.acquired = true
                    points += 1
                    pointsView.text = "points: $points"
                    Log.d("pointUp", "pacman got $points")
                    victoryCondition()
                }
            }
        }
        for (enemy in enemies) {
            if (distance(enemy.posX, enemy.posY, pacx.toFloat(), pacy.toFloat()) < 30) {
                time = 0
                running = false
                Toast.makeText(this.context, "The ghost haunted you, game over!", Toast.LENGTH_SHORT).show()
                levelUp = 0
            }
        }
    }

    fun victoryCondition() {
        for (coin in coins) {
            if (!coin.acquired)
                return
        }
        running = false
        Toast.makeText(this.context, "You acquired all the coins, well played!", Toast.LENGTH_SHORT).show()
        levelUp += 1
    }

    fun timer() {
        if (time > 0) {
            time -= 1
            timerView.text = "time left: $time"
        }
        else {
            Toast.makeText(this.context, "Game Over", Toast.LENGTH_SHORT).show()
            running = false
            levelUp = 0
        }

        for (enemy in enemies) {
            var randomDirection = Random().nextInt(4)
            enemy.direction = randomDirection + 1
            Log.d("EnemyDir", "${enemy.direction}")
        }
    }

}