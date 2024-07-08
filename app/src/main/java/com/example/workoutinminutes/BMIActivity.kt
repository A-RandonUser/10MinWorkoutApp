package com.example.workoutinminutes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.workoutinminutes.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val Metric_Units_View = "Metric_Unit_View"
        private const val US_Units_View = "US_Unit_View"
    }

    private var currentVisibleView : String = Metric_Units_View

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        MakeMetricUnitsViewVisible()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->

            if(checkedId == R.id.rbMetricUnits){
                MakeMetricUnitsViewVisible()
            }else{
                MakeUsUnitsViewVisible()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener{
            calculateUnits()
        }

    }

    private fun MakeMetricUnitsViewVisible(){
        currentVisibleView = Metric_Units_View
        binding?.tilUnitHeight?.visibility = View.VISIBLE
        binding?.tilUnitWeight?.visibility = View.VISIBLE

        binding?.tilUnitUsWeight?.visibility = View.GONE
        binding?.tilUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilUsUnitHeightInch?.visibility= View.GONE

        binding?.etUnitHeight?.text!!.clear()
        binding?.etUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun MakeUsUnitsViewVisible(){

        currentVisibleView = US_Units_View
        binding?.tilUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUnitWeight?.visibility = View.INVISIBLE

        binding?.tilUnitUsWeight?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightInch?.visibility= View.VISIBLE

        binding?.etUSUnitHeightInch?.text!!.clear()
        binding?.etUSUnitHeightFeet?.text!!.clear()
        binding?.etUnitUsWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi: Float){

        val bmiLabel : String
        val bmiDescription: String

        if(bmi.compareTo(15f)<= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "GOD DAMN! You really need to take better care of yourself! Eat MOOOAR!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f)<=0 ){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat MOOOAR!"
        }else if (bmi.compareTo(16f)> 0 && bmi.compareTo(18.5f)<=0){
            bmiLabel = "Underweight"
            bmiDescription = "GOD DAMN! You really need to take better care of yourself! Eat MOOOAR!"
        }else if (bmi.compareTo(18.5f)>0 && bmi.compareTo(25f)<=0){
            bmiLabel = "Normal"
            bmiDescription = "You are in good shape"
        }else if (bmi.compareTo(25f)>0 && bmi.compareTo(30f)<=0) {
            bmiLabel = "Overweight"
            bmiDescription = "You are slightly overweight you should exercise a bit more"
        }else if (bmi.compareTo(30f)>0 && bmi.compareTo(35f)<=0) {
            bmiLabel = "Obese Class I (Moderately obese) "
            bmiDescription = "You Should obese you should exercise more and watch your diet"
        }else if (bmi.compareTo(35f)>0 && bmi.compareTo(40f)<=0) {
            bmiLabel = "Obese Class II (Severely obese)"
            bmiDescription =
                "God DAMN you should stop eating those donuts and start exercising and dieting its not good for your health"
        }else {
            bmiLabel = "Obese class III (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act Now!"
        }
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility= View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIDescription?.text = bmiDescription
        binding?.tvBMIType?.text = bmiLabel

    }

    private fun validateUnit():Boolean{
        var isValid = true

        if(binding?.etUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }
    //first we check which unit is visible
    private fun calculateUnits(){
        if(currentVisibleView == Metric_Units_View){
            if(validateUnit()){
                val heightValue:Float = binding?.etUnitHeight?.text.toString().toFloat() / 100

                val weightValue:Float = binding?.etUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResult(bmi)

            }
        }else {
            if(validateUsUnit()){
                val usUnitFeetValue: String = binding?.etUSUnitHeightFeet?.text.toString()

                val usUnitInchValue: String = binding?.etUSUnitHeightInch?.text.toString()

                val usUnitWeightValue: Float = binding?.etUnitUsWeight?.text.toString().toFloat()

                val heightValue = usUnitInchValue.toFloat() + usUnitFeetValue.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)

            }else {
                Toast.makeText(this@BMIActivity, "Please Enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUsUnit():Boolean{
        var isValid = true

        when{
            binding?.etUSUnitHeightFeet?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUSUnitHeightInch?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUnitUsWeight?.text.toString().isEmpty() ->{
                isValid = false
            }
        }
        return isValid
    }
}