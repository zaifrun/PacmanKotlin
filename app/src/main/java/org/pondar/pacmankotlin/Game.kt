package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView)  {


    private var pointsView: TextView = view
    private var points: Int = 0
    private var goldCoin: GoldCoin? = null
    var running = false
    var direction = right



    //bitmap of the pacman
    var pacBitmap : Bitmap
    var pacx: Int = 0
    var pacy: Int = 0

    fun rng(): Int {
        val rngone = (10..1050).shuffled().first()
        println(rngone)
        return rngone }

    var coin2 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
    var coin3 = GoldCoin(coinx = rng(), coiny = rng(), false)
    //did we initialize the coins?
    var coinsInitialized = true

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()


    //DO Stuff to initialize the array list with some coins.


    init {

        coins.add(coin2)
        coins.add(coin3)
    }


    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }


    //TODO initialize goldcoins also here
    fun initializeGoldcoins()  {
        println("ArrayList MFFF" + coins.size)
        coinsInitialized = false
        var coin4 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
        var coin5 = GoldCoin(coinx = rng(), coiny = rng(), false)
        coins.add(coin4)
        coins.add(coin5)


    }


    fun newGame() {
        pacx = 200
        pacy = 200
        //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = true
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} ${points}"
        gameView?.invalidate() //redraw screen

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }
    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w && pacx < 1084) {
            pacx = pacx + pixels
        }
    }

    fun movePacmanLeft(pixels: Int) {
        if (pacx + pixels + pacBitmap.width < w && pacx > 0) {
            pacx = pacx - pixels
        }
    }

    fun movePacmanUp(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h && pacy > 1) {
            pacy = pacy - pixels
        }
    }

    fun movePacmanDown(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h && pacy < 1090) {
            pacy = pacy + pixels
        }
    }
    fun movedir (direction : Int){
        when(direction) {
            1 -> movePacmanRight(10)
            2 -> movePacmanLeft(10)
            3 -> movePacmanUp(10)
            4 -> movePacmanDown(10)
        }
        doCollisionCheck()
        gameView!!.invalidate()

    }


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman


    fun distance(x1: Int, x2: Int, y1: Int, y2: Int): Float {
        var xdist = x1 - x2.toDouble()
        var ydist = y1 - y2.toDouble()
        var result = Math.sqrt(xdist * xdist + ydist * ydist).toFloat()
        return result
    }

    fun doCollisionCheck()  {
        for (i in coins.indices) {
            var pacCenterX = pacx + pacBitmap.width / 2
            var pacCenterY = pacy + pacBitmap.height / 2
            var coinCenterX = coins.get(i).coinx + 60 / 2
            var coinCenterY = coins.get(i).coiny + 60 / 2
            var result = distance(pacCenterX, coinCenterX, pacCenterY, coinCenterY)

            if (result < 115f && !coins[i].taken) {

                points++
                pointsView.text = "${context.resources.getString(R.string.points)} ${points}"
                coins[i].taken = true
                println(coins[i].taken)
                println(points)
            } }
        if (points == 3) {
            val toasty = Toast.makeText(context.applicationContext, "Du har vundet", Toast.LENGTH_SHORT)
            toasty.show()
        }
    }
}












































