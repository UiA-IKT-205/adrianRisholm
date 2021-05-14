package xyz.wannamaker.tictactoe.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.wannamaker.tictactoe.databinding.DialogCreateGameBinding
import xyz.wannamaker.tictactoe.databinding.DialogJoinGameBinding
import java.lang.ClassCastException
import java.lang.IllegalStateException

class JoinGameDialog : DialogFragment() {

    private lateinit var listener: GameDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GameDialogListener
        }
        catch (e:ClassCastException) {
            throw ClassCastException(("$context must implement GameDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogJoinGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Join Game")
                setPositiveButton("Join") {dialog, lol ->
                    if(binding.username.text.toString() != "") {
                        listener.onDialogJoinGame(binding.username.text.toString(), binding.gameId.text.toString())
                    }
                }

                setNegativeButton("Cancel") { dialog, lol ->
                    dialog.cancel()
                }
                setView(binding.root)
            }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}