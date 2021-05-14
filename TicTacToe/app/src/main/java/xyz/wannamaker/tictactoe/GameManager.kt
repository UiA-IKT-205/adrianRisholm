package xyz.wannamaker.tictactoe

import android.content.Intent
import android.util.Log
import xyz.wannamaker.tictactoe.App.Companion.context
import xyz.wannamaker.tictactoe.data.Game
import xyz.wannamaker.tictactoe.data.GameState

typealias GameCallback = (game:Game?) -> Unit

object GameManager {

    private const val TAG:String = "GameManager"

    private val StartingGameState:GameState = listOf(mutableListOf('0','0','0'), mutableListOf('0','0','0'), mutableListOf('0','0','0'))

    fun createGame(player:String){
        GameService.createGame(player, StartingGameState) { game: Game?, err: Int? ->
            if (err != 0) {
                Log.e(TAG, "Failed to create game: $err")
            }
            else {
                if(game != null) {
                    Log.d(TAG, "Created game with player: $player")
                    val intent = Intent(context, GameActivity::class.java)
                    intent.putExtra("game", game)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            }
        }
    }

    fun joinGame(player:String, gameId: String){
        GameService.joinGame(player, gameId) { game: Game?, err: Int? ->
            if (err != 0) {
                Log.e(TAG, "Failed to join game: $err")
            }
            else {
                if(game != null) {
                    Log.d(TAG, "Joined game with gameId: $gameId, and player: $player")
                    val intent = Intent(context, GameActivity::class.java)
                    intent.putExtra("game", game)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    context.startActivity(intent)
                }
            }
        }
    }

    fun updateGame(gameId:String, state:GameState){
        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if (err != 0) {
                Log.e(TAG, "error updating game: $err")
            }
            else {
                Log.d(TAG, "Updated game with ID: $gameId")
            }
        }
    }

    fun pollGame(gameId:String, callback:GameCallback){
        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if (err != 0) {
                Log.e(TAG, "Error getting game data: $err")
            }
            else {
                //Log.d(TAG, "recieved game data from game: $gameId")
                    Log.d(TAG, game!!.state.toString())
                callback(game)
            }
        }
    }


}