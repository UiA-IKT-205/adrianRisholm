package xyz.wannamaker.tictactoe.dialog

interface GameDialogListener {
    fun onDialogCreateGame(player: String)
    fun onDialogJoinGame(player: String, gameId: String)
}