package xyz.wannamaker.tictactoe

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import xyz.wannamaker.tictactoe.data.Game
import xyz.wannamaker.tictactoe.data.GameState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


typealias GameServiceCallback = (state: Game?, errorCode:Int?) -> Unit

object GameService {

    const val TAG:String = "GameService"

    private val context = App.context

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    private enum class APIEndpoints(val url:String) {
        CREATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain), context.getString(R.string.base_path))),
        //how do concat game id into urls??
        //JOIN_GAME("%1s%2s%3s%4s".format(context.getString(R.string.protocol), context.getString(R.string.domain), context.getString(R.string.base_path),context.getString(R.string.join_path))),
        //UPDATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain), context.getString(R.string.base_path)))
    }

    fun createGame(playerId:String, state:GameState, callback: GameServiceCallback) {

        val url  = APIEndpoints.CREATE_GAME.url

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state", state)

        val request = object : JsonObjectRequest(Request.Method.POST, url, requestData,
            {
                val json = it

                val players = Json.decodeFromString<MutableList<String>>(json.get("players").toString())
                val gameId = json.get("gameId").toString()
                val gameState = Json.decodeFromString<List<MutableList<Char>>>(json.get("state").toString())

                val game = Game(players, gameId, gameState)
                callback(game, 0)
            },
            {
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {

                return setHeaders()
            }
        }

        requestQueue.add(request)
    }

    fun  joinGame(playerId:String, gameId:String, callback: GameServiceCallback) {

        val url = APIEndpoints.CREATE_GAME.url.plus("/$gameId").plus("/Join")


        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(Request.Method.POST, url, requestData,
            {

                val json = it
                val players = Json.decodeFromString<MutableList<String>>(json.get("players").toString())
                val gameId = json.getString("gameId")
                val gameState = Json.decodeFromString<List<MutableList<Char>>>(json.get("state").toString())


                val game = Game(players, gameId, gameState)
                callback(game, 0)
            },
            {
                callback(null, it.networkResponse.statusCode)
            }) {
            override fun getHeaders(): MutableMap<String, String> {

                return setHeaders()
            }

        }

        requestQueue.add(request)
    }
    fun updateGame(gameId:String, state:GameState, callback: GameServiceCallback) {
        val url = APIEndpoints.CREATE_GAME.url.plus("/$gameId").plus("/update")
        val requestData = JSONObject()
        requestData.put("gameId", gameId)
        requestData.put("state", state)

        val request = object : JsonObjectRequest(Method.POST,url, requestData,
            {
                val json = it

                val players = Json.decodeFromString<MutableList<String>>(json.get("players").toString())
                val gameId = json.getString("gameId")
                val gameState = Json.decodeFromString<List<MutableList<Char>>>(state.toString())

                val game = Game(players, gameId, gameState)
                callback(game,0)
            }, {
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                return setHeaders()
            }
        }
        requestQueue.add(request)

    }
    fun  pollGame(gameId:String, callback: GameServiceCallback) {
        val url = APIEndpoints.CREATE_GAME.url.plus("/$gameId").plus("/poll")
        val requestData = JSONObject()

        val request = object : JsonObjectRequest(Method.GET,url, requestData,
            {
                val json = it

                val players = Json.decodeFromString<MutableList<String>>(json.get("players").toString())
                val gameState = Json.decodeFromString<List<MutableList<Char>>>(json.get("state").toString())

                val game = Game(players, gameId, gameState)
                callback(game,0)
            }, {
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                return setHeaders()
            }
        }
        requestQueue.add(request)
    }

    fun setHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Game-Service-Key"]= context.getString(R.string.game_service_key)
        return headers
    }


}