package xyz.wannamaker.tictactoe

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import xyz.wannamaker.tictactoe.data.Game
import xyz.wannamaker.tictactoe.data.GameState
import xyz.wannamaker.tictactoe.databinding.ActivityGameBinding
import kotlin.system.exitProcess

class GameActivity : AppCompatActivity() {

    private val TAG:String = "GameActivity"

    private lateinit var binding: ActivityGameBinding

    private lateinit var handler: Handler

    var playerTurn: Boolean = false
    var playerIcon: Char = ' '
    var opponentIcon: Char = ' '


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var game: Game? = intent.getParcelableExtra("game")

        game?.let {
            updateButtons(it.state)
        }
        game?.let {
            setGameInfo(it)
        }
        initializeButtons(game)

        binding.gameid.text = game?.gameId.toString()

        handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                GameManager.pollGame(game?.gameId.toString()) { localGame: Game? ->

                    if (game?.players != localGame?.players && localGame != null) {
                        binding.player1name.text = localGame.players[0] + " - X"
                        binding.player2name.text = localGame.players[1] + " - O"
                    }

                    if(game?.state != localGame?.state && localGame != null) {

                        game = localGame
                        initializeButtons(game)
                        updateButtons(game!!.state)
                        checkWinCondition(game!!.state)
                        Thread.sleep(30)
                        playerTurn = true

                    }
                }
                handler.postDelayed(this, 200)

            }

        })
    }



    private fun spendTurn(game: Game?, x: Int, y: Int) {
        if (playerTurn) {
            if (game != null && game.state[x][y] == '0') {
                game.state[x][y] = playerIcon
                game.state.let {
                    GameManager.updateGame(game.gameId, it)
                }


                checkWinCondition(game.state)
                Thread.sleep(30)
                playerTurn = false




            }

        }
        updateButtons(game!!.state)

    }
    fun updateButtons(state: GameState) {

        binding.zero0.text = formatValue(state[0][0])
        binding.zero1.text = formatValue(state[0][1])
        binding.zero2.text = formatValue(state[0][2])


        binding.one0.text = formatValue(state[1][0])
        binding.one1.text = formatValue(state[1][1])
        binding.one2.text = formatValue(state[1][2])


        binding.two0.text = formatValue(state[2][0])
        binding.two1.text = formatValue(state[2][1])
        binding.two2.text = formatValue(state[2][2])

    }

    fun formatValue(char: Char): String {
        if (char == '0') return " "
        else {
            Log.d(TAG, "CHARACTER FORMATTED: $char")
            return char.toString()

        }
    }

    fun setGameInfo(game: Game) {
        if (game.players.size == 1) {
            playerIcon = 'X'
            opponentIcon = 'O'
            binding.player1name.text = game.players[0] + " - $playerIcon"
            playerTurn = true
        }
        else {
            playerIcon = 'O'
            opponentIcon = 'X'
            binding.player1name.text = game.players[0] + " - $playerIcon"
            binding.player2name.text = game.players[1] + " - $opponentIcon"
        }

    }


    private fun checkWinCondition(state: GameState) {
        var gameWon = false
        //Horizontals

        for (i in 0..2) {
            if((state[i][0] == state[i][1]) && (state[i][0] == state[i][2]) && (state[i][0] != '0')) gameWon = true
        }

        //Verticals
        for (i in 0..2) {
            if((state[0][i] == state[1][i]) && (state[0][i] == state[2][i]) && (state[i][0] != '0')) gameWon = true
        }

        //Diagonals, harder to do with loops..
        if((state[0][0] == state[1][1]) && (state[0][0] == state[2][2]) && (state[0][0] != '0')) gameWon = true
        if((state[2][0] == state[1][1]) && (state[2][0] == state[0][2]) && (state[2][0] != '0')) gameWon = true

        //easy was to check for draw, as there are only a maximum of 9 moves in Tic Tac Toe
        var drawcount: Int = 0
        for(i in 0..2) {
            for (j in 0..2) {
                if (state[i][j] != '0') drawcount+=1
            }
        }

        if (drawcount>=9) winAlert("draw")


        if (playerTurn && gameWon) {
            winAlert("player1")
        }

        else if(!playerTurn && gameWon) {
            winAlert("player2")
        }




    }

    fun winAlert(winner: String) {


        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            if (winner=="player1") {
                setTitle("Winner!")
                setMessage("Congratulations, you won!")
                setPositiveButton("Cool") {
                        _,_ ->
                    handler.looper.quit()
                    val intent = Intent(this.context, MainActivity::class.java)
                    startActivity(intent)

                }
                show()
            }
            if (winner == "player2") {
                setTitle("Loser!")
                setMessage("You lost! :( :( :) :(")
                setPositiveButton("Sadge") {
                        _,_ ->

                    //This doesn't work, it keeps polling
                    handler.looper.quit()

                    //This also doesnt work, returns to a gameactivity with null values instead of mainactivity.. i dunno.
                    val intent = Intent(this.context, MainActivity::class.java)
                    startActivity(intent)
                }
                show()
            }

            if (winner == "draw") {
                setTitle("Draw!")
                setMessage("The game ended in a draw, well played! :( :( :) :(")
                setPositiveButton("OK") {
                        _,_ ->
                    handler.looper.quit()
                    val intent = Intent(this.context, MainActivity::class.java)
                    startActivity(intent)
                }
                show()
            }

        }
    }

    fun initializeButtons(game: Game?) {
        // please teach me a better way to iterate over this many buttons :D
        binding.zero0.setOnClickListener {
            spendTurn(game, 0, 0)
        }
        binding.zero1.setOnClickListener {
            spendTurn(game, 0, 1)
        }
        binding.zero2.setOnClickListener {
            spendTurn(game, 0, 2)
        }

        binding.one0.setOnClickListener {
            spendTurn(game, 1, 0)
        }
        binding.one1.setOnClickListener {
            spendTurn(game, 1, 1)
        }
        binding.one2.setOnClickListener {
            spendTurn(game, 1, 2)
        }

        binding.two0.setOnClickListener {
            spendTurn(game, 2, 0)
        }
        binding.two1.setOnClickListener {
            spendTurn(game, 2, 1)
        }
        binding.two2.setOnClickListener {
            spendTurn(game, 2, 2)
        }
    }
}