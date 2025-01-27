package com.example.workoutinminutes

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutinminutes.databinding.ActivityExerciseBinding
import com.example.workoutinminutes.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var ExerciseTimerDuration: Long = 1

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts:TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //we gonna make an action bar /tool bar
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        exerciseList = Constants.defaultExercise()
        //setting up the text to speach
        tts = TextToSpeech(this,this)


        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onBackPressed() {
        customDialogForBackButton()

    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }


    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.example.workoutinminutes/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }


        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTittle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility =View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpComingLabel?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.VISIBLE
        
        if(restTimer !=null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvUpComingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()

        //setting up tts
        speakOut("You can rest now ")

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        //make the progress bar insibisble or gone from the ui
        //if we use gone the constraint for tvtittle
        // is gone cuz the framelayout gets deleted and we need to add it again to use gone
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTittle?.visibility = View.INVISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.INVISIBLE
        binding?.tvUpComingLabel?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility =View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        if(exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        //setting up the tts
        //!! force on repit
        speakOut(exerciseList!![currentExercisePosition].getName())


        //setting up the exercise
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()


        setExerciseProgressBar()
    }

    private fun setRestProgressBar (){
        binding?.progressBar?.progress = restProgress

        restTimer = object:CountDownTimer(restTimerDuration*1000,1000){
            //every 1000miliseconds do this code
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 15 - restProgress
                binding?.tvTimer?.text = (15 - restProgress).toString()
            }
            //once the ticking is done call this
            override fun onFinish() {
                currentExercisePosition++
                //changing the color of the progress circles
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar (){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object:CountDownTimer(ExerciseTimerDuration*1000,1000){
            //every 1000miliseconds do this code
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 45 - exerciseProgress
                binding?.tvExerciseTimer?.text = (45 - exerciseProgress).toString()
            }
            //once the ticking is done call this
            override fun onFinish() {

                //proverqvame dali ima ostanali exercises
                if (currentExercisePosition < exerciseList?.size!! - 1)
                {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                   finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }

            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer !=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        //shutting down the text to speach feature when activity is destroyed
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        //shutting down the player
        if(player != null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            //set US english as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported!")
            }
        }else {
            Log.e("TTS","Initializations Failed!")
        }

    }

    private fun speakOut (text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

}