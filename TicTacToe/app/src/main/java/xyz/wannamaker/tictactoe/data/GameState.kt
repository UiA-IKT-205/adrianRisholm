package xyz.wannamaker.tictactoe.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias GameState = List<MutableList<Char>>

@Parcelize
data class Game (val players: MutableList<String>, val gameId:String, val state:GameState):Parcelable