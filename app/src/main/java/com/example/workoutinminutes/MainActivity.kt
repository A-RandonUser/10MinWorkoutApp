package com.example.workoutinminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workoutinminutes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    //we can access only the xml files from the activity main
    // so in case we have the same id in different xml we dont acess it by mistake
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //the constraint layout activity
        // main is the root of this entire binding object
        // but now instead of using findviewbyid we use
        //old aprouch
        //val flStartButton : FrameLayout = findViewById(R.id.flStart)
        //we access the IDs inside the binding object so no need to create another object
        //we use the ? because they are nullable
        //when we click the start button we wanna move over to the exercise activity
        //we do that by creating an intent
        binding?.flStart?.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }


    }

    //how to unassign the binding to avoid memory leak
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}