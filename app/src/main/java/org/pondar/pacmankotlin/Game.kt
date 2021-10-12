package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.widget.TextView
import java.util.*
import kotlin.math.*


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0
        //bitmap of the pacman
        var pacBitmap: Bitmap
        var coinBitmap: Bitmap
        var enemyBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0

        var timer: Timer? = null
        var timer2: Timer? = null

        var running = true


        var currentRotateValue = 0F
        var moving = 20

        //did we initialize the coins?
        var coinsInitialized = false

        //Resets coins
        var coinReset = 0

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()

        //enemies
        var enemies = ArrayList<Enemy>()
        var enemyCount = 0

        //a reference to the gameview
        private lateinit var gameView: GameView
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        pacBitmap = Bitmap.createScaledBitmap(pacBitmap, 150, 150, true)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, 110, 110, true)
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, 150, 150, true)
    }


    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins(numberOfCoins: Int)
    {
        //DO Stuff to initialize the array list with some coins.
        repeat(numberOfCoins) {
            var maxWidth = gameView.w - coinBitmap.width * 2
            var maxHeight = gameView.h - coinBitmap.height * 2
            var x = (0 + coinBitmap.width..maxWidth).random();
            var y = (0 + coinBitmap.height..maxHeight).random();
            coins.add(GoldCoin(x, y))
        }
        coinsInitialized = true
    }

    fun initializeEnemies() {
        var x = (0..w - 100).random();
        var y = (0..h - 100).random();
        enemies.add(Enemy(x, y))
    }

    fun reset() {
        pacx = 50
        pacy = 400
        coins.clear()
        coinReset = 0
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        enemyCount  = 0
        enemies.clear()
        gameView.invalidate()
        if(timer2 != null){

            timer2?.cancel()
            timer2?.purge()
        }
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun stopPacmanMovement() {
        timer?.cancel()
        timer?.purge()
    }
    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        if (currentRotateValue == angle) {
            return pacBitmap
        }
        var rotateValue = angle - currentRotateValue
        currentRotateValue = angle

        val matrix = Matrix()
        matrix.preRotate(rotateValue)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    fun movePacman(pacDir: Int) {
        stopPacmanMovement()
        when (pacDir) {
            0 -> pacBitmap = rotateBitmap(pacBitmap, 0F)
            1 -> pacBitmap = rotateBitmap(pacBitmap, 180F)
            2 -> pacBitmap = rotateBitmap(pacBitmap, 270F)
            3 -> pacBitmap = rotateBitmap(pacBitmap, 90F)
        }

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (running) {
                    when (pacDir) {
                        0 -> movePacmanRight()
                        1 -> movePacmanLeft()
                        2 -> movePacmanUp()
                        3 -> movePacmanDown()
                    }
                    doCollisionCheck()

                    gameView.invalidate()
                }
            }
        }, 0, 50)

    }

    fun movePacmanRight() {
        //still within our boundaries?
        if (pacx + moving + pacBitmap.width < w) {
            pacx = pacx + moving
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanLeft() {
        //still within our boundaries?
        if (pacx - moving > 0) {
            pacx = pacx - moving
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanUp() {
        //still within our boundaries?
        if (pacy - moving > 0) {
            pacy = pacy - moving
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun movePacmanDown() {
        //still within our boundaries?
        if (pacy + moving + pacBitmap.height < h) {
            pacy = pacy + moving
            doCollisionCheck()
            gameView.invalidate()
        }
    }
    fun moveEnemies() {
        if (enemyCount <= 5) {
            var random = 0
            timer2 = Timer()
            timer2?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (running) {
                        random = (0..3).random()
                    }
                }
            }, 0, 1500)


            enemies.forEach {

                if (!it.kill) {
                    it.kill = true
                    timer2?.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            if (running) {
                                when (random) {
                                    0 -> borderCollisionCheck(it, 0)
                                    1 -> borderCollisionCheck(it, 1)
                                    2 -> borderCollisionCheck(it, 2)
                                    3 -> borderCollisionCheck(it, 3)
                                }
                                gameView.invalidate()
                            }
                        }
                    }, 0, 150)
                }
            }
        }
    }


    //CollisionFormula helper function
    fun collisionFormula(pacX: Int, pacY: Int, objX: Int, objY: Int, width: Int, height: Int): Double {
        var compareX = (objX - (pacX + (width / 2))).toDouble().pow(2)
        var compareY = (objY - (pacY + (height / 2))).toDouble().pow(2)
        var compare = compareX + compareY
        var distance = sqrt(compare)
        return distance
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        coins.forEach{
            if(!it.taken){
                var distance = collisionFormula(pacx, pacy, it.coinX, it.coinY, pacBitmap.width, pacBitmap.height)
                if(distance < 120){
                    coinReset++
                    points += 50
                    it.taken = true
                    pointsView.post(Runnable {
                        pointsView.text = "${context.resources.getString(R.string.points)} $points"
                    })
                    gameView.invalidate()
                }
                if(coinReset == 5) {
                    coinsInitialized = false
                    coinReset = 0
                    if(enemyCount < 5){
                        initializeEnemies()
                        enemyCount++
                    }
                    moveEnemies()
                }
            }
        }
        enemies.forEach{
            var distance = collisionFormula(pacx, pacy, it.enemyX, it.enemyY, pacBitmap.width, pacBitmap.height)
            if (distance < 80) {
                gameView.post(Runnable {
                    reset()
                })
                gameView.invalidate()
            }
        }
    }
    fun borderCollisionCheck(obj: Enemy, direction: Int) {
        when (direction) {
            // down
            0 -> if (obj.enemyY + moving + enemyBitmap.height < h) obj.enemyY += moving
            // up
            1 -> if (obj.enemyY - moving > 0) obj.enemyY -= moving
            // left
            2 -> if (obj.enemyX - moving > 0) obj.enemyX -= moving
            // right
            3 -> if (obj.enemyX + moving + enemyBitmap.width < w) obj.enemyX += moving
        }
    }

}