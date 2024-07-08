package com.example.workoutinminutes

object Constants {
    fun defaultExercise ():ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(
            1,"Jumping Jacks",R.drawable.jumpingjacks,false,false
        )
        exerciseList.add(jumpingJacks)

        val lunges = ExerciseModel(
            2,"Lunges",R.drawable.lunge,false,false
        )
        exerciseList.add(lunges)

        val pushups = ExerciseModel(
            3,"Push Ups",R.drawable.pushups,false,false
        )
        exerciseList.add(pushups)

        val plank = ExerciseModel(
            4,"Plank",R.drawable.plank,false,false
        )
        exerciseList.add(plank)

        val russiantwists = ExerciseModel(
            5,"Russian Twists",R.drawable.russiantwists,false,false
        )
        exerciseList.add(russiantwists)
        val tricepdips = ExerciseModel(
            6,"Tricep Dips",R.drawable.tricepdip,false,false
        )
        exerciseList.add(tricepdips)
        val squats = ExerciseModel(
            7,"Squats",R.drawable.squat,false,false
        )
        exerciseList.add(squats)

        val wallsit = ExerciseModel(
            8,"Wall Sit",R.drawable.wallsit,false,false
        )
        exerciseList.add(wallsit)
        val stepups = ExerciseModel(
            9,"Step Ups",R.drawable.stepups,false,false
        )
        exerciseList.add(stepups)

        val cobrastretch = ExerciseModel(
            10,"Cobra Stretch",R.drawable.cobrastretch,false,false
        )
        exerciseList.add(cobrastretch)

        return exerciseList
    }
}