package com.example.piano.data

data class Note(val value:String, val start:Long, val stop:Long) {

    override fun toString(): String {
        return "$value, $start, $stop"
    }
}
