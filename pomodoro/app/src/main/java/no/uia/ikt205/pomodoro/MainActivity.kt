package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.R.id.setPauseTimeseekBar
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 10000L
    var timeToCountDownInMsInput = 10000L
    var pauseTimeInMs = 5000L;
    val timeTicks = 1000L
    var numberOfPauses = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById<Button>(R.id.startCountdownButton)
       startButton.setOnClickListener(){
           startCountDown(it,  false)
       }
       coutdownDisplay = findViewById<TextView>(R.id.countDownView)


        val setTimeseekBar = findViewById<SeekBar>(R.id.setTimeseekBar);
        setTimeseekBar.max = 285;

        setTimeseekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val min = 15;
                timeToCountDownInMsInput = minutesToMilliSeconds(progress.toLong() + min);
                setCountDownTime(timeToCountDownInMsInput);

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        val setPauseTimeseekBar = findViewById<SeekBar>(setPauseTimeseekBar);
        setPauseTimeseekBar.max = 55;

        setPauseTimeseekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val min = 5;
                pauseTimeInMs = minutesToMilliSeconds(progress.toLong() + min);
                updateCountDownDisplay(pauseTimeInMs);

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        val numberOfPausesInput = findViewById<EditText>(R.id.numberOfPausesText);
        numberOfPausesInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    numberOfPauses = s.toString().toInt()
                }
                else {
                    numberOfPauses = 0
                    numberOfPausesInput.setText("0")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    fun setCountDownTime(newCountDownTimeInMs:Long){
        timeToCountDownInMs = newCountDownTimeInMs
        updateCountDownDisplay(timeToCountDownInMs)
    }

    fun startCountDown(v: View, boolean: Boolean){
        val paused = boolean;

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {


                if (!paused) {
                    if (numberOfPauses == 0) {
                        Toast.makeText(this@MainActivity, "Arbeidsøkt er over! Du er ferdig", Toast.LENGTH_SHORT).show()
                        v.isEnabled = true
                    }
                    else {
                        setCountDownTime(pauseTimeInMs)
                        //start pause
                        startCountDown(startButton, true)
                        numberOfPauses--
                        Toast.makeText(this@MainActivity, "Pause begynner nå!", Toast.LENGTH_SHORT).show()
                    }


                }
                else {
                    setCountDownTime(timeToCountDownInMsInput)
                    //start økt
                    if (numberOfPauses > 0) {
                        startCountDown(startButton, false)
                        Toast.makeText(this@MainActivity, "Pausen er ferdig! Du har $numberOfPauses pauser igjen. Ny arbeidsøkt begynner", Toast.LENGTH_SHORT).show()
                    }
                    else if (numberOfPauses == 0) {
                        startCountDown(startButton, false)
                        Toast.makeText(this@MainActivity, "Pausen er ferdig! Starter siste arbeidsøkt nå.", Toast.LENGTH_SHORT).show()
                    }
                }





            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }
        v.isEnabled = false
        timer.start()
    }

    fun startPause() {
        timer =  object : CountDownTimer(pauseTimeInMs, timeTicks) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Pausen er ferdig! Du har: $numberOfPauses pauser igjen. Arbeid begynner",Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}