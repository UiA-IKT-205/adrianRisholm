package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val halfTones = listOf("C#","D#","F#","G#","A#","C2#","D2#","F2#","G2#","A2#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullTones.forEach{
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            fullTonePianoKey.onKeyDown = { note ->
                startPlay = System.nanoTime()
                println("Piano key down $note")
            }

            fullTonePianoKey.onKeyUp = {
                var stopPlay = System.nanoTime() - startPlay

                val note = Note(it, startPlay, stopPlay)
                println("Piano key up $note")
            }

            ft.add(view.pianoKeys.id, fullTonePianoKey, "note_$it")
        }

        halfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            halfTonePianoKey.onKeyDown = { note ->
                startPlay = System.nanoTime()
                println("Piano key down $note")
            }

            halfTonePianoKey.onKeyUp = {
                var stopPlay = System.nanoTime() - startPlay

                val note = Note(it, startPlay, stopPlay)
                println("Piano key up $note")
            }

            ft.add(view.pianoKeys.id, halfTonePianoKey, "note_$it")
        }

        ft.commit()

        /*view.saveScoreBtn.setOnClickListener {
            val fileName = view.fileNameTextEdit.text.toString()
        }*/

        return inflater.inflate(R.layout.fragment_piano, container, false)
    }
}