package com.example.bottomnavbar.model

import kotlin.random.Random

class SimonGameModel {
    fun generateSequence(level: Int): List<String> {
        val colors = listOf("rouge", "vert", "bleu", "jaune")
        return List(level) { colors[Random.nextInt(colors.size)] }
    }
}
