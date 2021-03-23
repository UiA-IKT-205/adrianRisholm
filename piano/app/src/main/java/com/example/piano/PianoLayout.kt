package com.example.piano

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val halfTones = listOf("C#","D#","F#","G#","A#","C2#","D2#","F2#","G2#","A2#")

    private var score:MutableList<Note> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        var melodyStart:Long = 0


        fullTones.forEach{
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            fullTonePianoKey.onKeyDown = { note ->
                if (score.isEmpty()) {
                        melodyStart = System.nanoTime()
                    }
                startPlay = System.nanoTime()
                println("Piano key down $note")
            }

            fullTonePianoKey.onKeyUp = {
                var stopPlay = System.nanoTime() - startPlay

                startPlay -= melodyStart
                val note = Note(it, startPlay, stopPlay)

                score.add(note)
                println("Piano key up $note")
            }

            ft.add(view.pianoKeys.id, fullTonePianoKey, "note_$it")
        }

        halfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            halfTonePianoKey.onKeyDown = { note ->
                if (score.isEmpty()) {
                    melodyStart = System.nanoTime()
                }
                startPlay = System.nanoTime()
                println("Piano key down $note")
            }

            halfTonePianoKey.onKeyUp = {
                var stopPlay = System.nanoTime() - startPlay


                startPlay -= melodyStart
                val note = Note(it, startPlay, stopPlay)

                score.add(note)
                println("Piano key up $note")
            }

            ft.add(view.pianoKeys.id, halfTonePianoKey, "note_$it")
        }

        ft.commit()

        view.saveScoreBtn.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            val path = this.activity?.getExternalFilesDir(null)

            //in the lecture it was said this if statement is not up to lecturers standards, would it be better to have multiple nested if statements? i think not, so it stays.
            if  (fileName.isNotEmpty() && path != null && score.isNotEmpty()) {
                fileName = "$fileName.mus"
                if(File(path, fileName).exists()) {
                    //could be made a function but cba
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this@PianoLayout.context)
                    alert.setTitle("Filename already in use")
                    alert.setMessage("Filename is already in use, please select another filename")
                    alert.setNeutralButton(
                        "Ok"
                    ) { _, _ ->
                        Toast.makeText(this@PianoLayout.context, "Alert dialog closed.", Toast.LENGTH_LONG).show()
                    }

                }
                else {
                    FileOutputStream(File(path,fileName), true).bufferedWriter().use { bw ->
                        score.forEach {
                            bw.write("${it.toString()}\n")
                        }
                    }
                }

            }
            else {
                /// TODO: What are you talking aboot
            }
        }

        return inflater.inflate(R.layout.fragment_piano, container, false)
    }
}