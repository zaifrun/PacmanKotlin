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
    private var eneMies: Enemies? = null
    var running = false

    //bitmap of the pacman
    var pacBitmap : Bitmap
    var pacx: Int = 0
    var pacy: Int = 0

    fun rng(): Int {
        val rngone = (10..1000).shuffled().first()
        println(rngone)
        return rngone }

    var coin2 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
    var coin3 = GoldCoin(coinx = rng(), coiny = rng(), false)
    var coin9 = GoldCoin(coinx = rng(), coiny = rng(), false)
    //did we initialize the coins?
    var coins = ArrayList<GoldCoin>()
    init {

        coins.add(coin2)
        coins.add(coin3)
        coins.add(coin9)
    }
    var coinsInitialized = true

    var skurke = ArrayList<Enemies>()

    var VAR = Enemies(enx=rng(),eny = rng(),dead = false)


    //the list of goldcoins - initially empty



    //DO Stuff to initialize the array list with some coins.




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

        var coin4 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
        var coin5 = GoldCoin(coinx = rng(), coiny = rng(), false)
        var coin10 = GoldCoin(coinx = rng(), coiny = rng(), false)
        coins.add(coin4)
        coins.add(coin5)
        coins.add(coin10)
        coinsInitialized = true

    }
    fun lvl(){
        if(points == 3){
            var coin6 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
            coins.add(coin6)
            var coin7 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
            coins.add(coin7)
            var coin8 = GoldCoin(coinx = rng(), coiny = rng(), taken = false)
            coins.add(coin8)


        }
        else if(points == 6){
            val toastyy = Toast.makeText(context.applicationContext, "BOSS LVL", Toast.LENGTH_SHORT)
            toastyy.show()
            var sepiroth = Enemies(enx=rng(),eny = rng(), dead = false)
            skurke.add(sepiroth)
            var sauron = Enemies(enx=rng(),eny = rng(), dead = false)
            skurke.add(sauron)
            var udlejer = Enemies(enx=rng(),eny = rng(), dead = false)
            skurke.add(udlejer)


        }
    }
    fun newGame() {
        pacx = 200
        pacy = 200
        //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        points = 0
        if(points > 0 || coinsInitialized == false)
        {coins.clear()}
        initializeGoldcoins()
        running = true
        pointsView.text = "${context.resources.getString(R.string.points)} ${points}"
        gameView?.invalidate() //redraw screen
    }




    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w ) {
            pacx = pacx + pixels

        }
    }

    fun movePacmanLeft(pixels: Int) {
        if (pacx + pixels + pacBitmap.width > 0 && pacx > 0 ) {
            pacx = pacx - pixels

        }
    }

    fun movePacmanUp(pixels: Int) {
        if (pacy + pixels + pacBitmap.height > 0 && pacy > 0 ) {
            pacy = pacy - pixels

        }
    }

    fun movePacmanDown(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h ) {
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



        println("pacman pos" + pacy+ pacx)

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
        val rpac = pacBitmap.width/2
        val rrpac = pacBitmap.height/2
        for (i in coins.indices) {
            var pacCenterX = pacx + rpac
            var pacCenterY = pacy + rrpac
            var coinCenterX = coins.get(i).coinx + 60 / 2
            var coinCenterY = coins.get(i).coiny + 60 / 2
            var result = distance(pacCenterX, coinCenterX, pacCenterY, coinCenterY)

            if (result < 115f && !coins[i].taken) {

                points++
                pointsView.text = "${context.resources.getString(R.string.points)} ${points}"
                coins[i].taken = true
                coins.drop(i)
                println(coins[i].taken)
                println(points)
            }
        }

        if (points == 3) {
            val toasty = Toast.makeText(context.applicationContext, "Difficulty up", Toast.LENGTH_SHORT)
            toasty.show() }
    }





}












































