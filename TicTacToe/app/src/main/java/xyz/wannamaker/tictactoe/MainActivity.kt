package xyz.wannamaker.tictactoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import xyz.wannamaker.tictactoe.databinding.ActivityMainBinding
import xyz.wannamaker.tictactoe.dialog.CreateGameDialog
import xyz.wannamaker.tictactoe.dialog.GameDialogListener
import xyz.wannamaker.tictactoe.dialog.JoinGameDialog

class MainActivity : AppCompatActivity(), GameDialogListener {

    private val TAG:String = "MainActivity"

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //done this too many times
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.creategamebtn.setOnClickListener {
            val dialog = CreateGameDialog()
            dialog.show(supportFragmentManager, "CreateGameDialogFragment")
        }

        binding.joingamebtn.setOnClickListener {
            val dialog = JoinGameDialog()
            dialog.show(supportFragmentManager, "JoinGameDialogFragment")
        }

    }
    override fun onDialogCreateGame(player: String) {
        Log.d(TAG, player)
        GameManager.createGame(player)

        //Small delay for API to keep up
        Thread.sleep(500)

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("playername", player)
        }

        startActivity(intent)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {

        GameManager.joinGame(player, gameId)

        Thread.sleep(500)

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("playername", player)
        }

        startActivity(intent)
    }
}