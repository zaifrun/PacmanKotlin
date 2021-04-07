package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View



//note we now create our own view class that extends the built-in View class
class GameView : View {


    private var game: Game? = null
    private var h: Int = 0
    private var w: Int = 0 //used for storing our height and width of the view

    fun setGame(game: Game?) {
        this.game = game
    }


    /* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    override fun onDraw(canvas: Canvas) {
        //Here we get the height and weight
        h = canvas.height
        w = canvas.width
        //update the size for the canvas to the game.
        game?.setSize(h, w)
        Log.d("GAMEVIEW", "h = $h, w = $w")

        //are the coins initiazlied?
        //if not initizlise them
        if (game!!.coinsInitialized)
            game!!.initializeGoldcoins()



        //Making a new paint object
        val paint = Paint()
        val circlepaint = Paint()
        circlepaint.color = Color.RED
        canvas.drawColor(Color.WHITE) //clear entire canvas to white color

        //draw the pacman
        canvas.drawBitmap(game!!.pacBitmap, game?.pacx!!.toFloat(),
                game?.pacy!!.toFloat(), paint)

        //TODO loop through the list of goldcoins and draw them here

        var radiusC = 40f
         Runnable {  for  (item in game!!.coins.indices) {
            if (!game!!.coins.get(item).taken)
                canvas.drawCircle(game!!.coins[item].coinx.toFloat(), game!!.coins[item].coiny.toFloat(), radiusC, circlepaint)
        }}

















        //game?.doCollisionCheck()

        super.onDraw(canvas)
    }
}








