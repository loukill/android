package com.example.bottomnavbar.viewModel

import com.example.bottomnavbar.model.SimonGameModel

class SimonGameViewModel(private val model: SimonGameModel) {
    var currentLevel = 1
    lateinit var currentSequence: List<String>


    fun generateNewSequence() {
        currentSequence = model.generateSequence(currentLevel)
    }

    fun checkPlayerSequence(playerSequence: List<String>): Boolean {
        return playerSequence == currentSequence
    }

    fun incrementLevel() {
        currentLevel++
    }

}
