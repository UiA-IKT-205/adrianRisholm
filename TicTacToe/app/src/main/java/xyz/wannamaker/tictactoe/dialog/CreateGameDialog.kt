package xyz.wannamaker.tictactoe.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.wannamaker.tictactoe.databinding.DialogCreateGameBinding
import java.lang.ClassCastException
import java.lang.IllegalStateException

class CreateGameDialog : DialogFragment() {

    internal lateinit var listener: GameDialogListener

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
            val binding = DialogCreateGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Create Game")
                setPositiveButton("Create") {dialog, lol ->
                    if(binding.username.text.toString() != "") {
                        listener.onDialogCreateGame(binding.username.text.toString())
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