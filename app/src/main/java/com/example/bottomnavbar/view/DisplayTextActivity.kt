package com.example.bottomnavbar.view

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.RetrofitClient
import com.example.bottomnavbar.model.Text
import com.example.bottomnavbar.model.TextCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayTextActivity : AppCompatActivity() {

    private var selectedTextId: String? = null
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_text)

        fetchTexts()

        val readButton: Button = findViewById(R.id.btnRead)
        readButton.setOnClickListener {
            selectedTextId?.let { textId ->
                playTextAudio(textId)
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun playTextAudio(textId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getAudioUrlByTextId(textId)
                if (response.isSuccessful && response.body() != null) {
                    val audioUrls = response.body()!!.audioUrls
                    if (audioUrls.isNotEmpty()) {
                        val audioUrl = audioUrls[0] // Jouer le premier fichier audio
                        withContext(Dispatchers.Main) {
                            mediaPlayer?.stop()
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(audioUrl)
                                prepare()
                                start()
                            }
                        }
                    } else {
                        Log.e("DisplayTextActivity", "No audio URLs received for text ID: $textId")
                    }
                } else {
                    Log.e("DisplayTextActivity", "Error fetching audio: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception in fetching audio", e)
            }
        }
    }



    private fun fetchTexts() {
        Log.d("DisplayTextActivity", "Fetching texts")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getAllText()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        setupSpinner(response.body()!!)
                    } else {
                        Log.e("DisplayTextActivity", "Error fetching texts: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception in fetching texts", e)
            }
        }
    }

    private fun setupSpinner(texts: List<Text>) {
        val spinner: Spinner = findViewById(R.id.spinnerTextCategory)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, texts.map { it.title })
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedText = texts[position]
                selectedTextId = selectedText._id
                fetchTextByCategory(selectedText._id)// Assurez-vous que 'id' est le bon champ
                // Vous pouvez appeler ici la méthode pour afficher le texte sélectionné
                consulterTexte(selectedText._id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Gestion du cas où aucun texte n'est sélectionné
            }
        }
    }

    private fun fetchTextByCategory(textId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getTextById(textId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val text = response.body()!!
                        Log.d("DisplayTextActivity", "Fetched text: ${text.contenu}")
                        val textView = findViewById<TextView>(R.id.tvTextContent)
                        textView.text = text.contenu
                    } else {
                        Log.e("DisplayTextActivity", "Error fetching text: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception in fetching text", e)
            }
        }
    }

    private fun consulterTexte(textId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.enregistrerConsultation(textId)
                Log.d("DisplayTextActivity", "Envoi de l'ID pour consultation: $textId")
                if (response.isSuccessful) {
                    Log.d("DisplayTextActivity", "Consultation enregistrée avec succès pour le texte ID: $textId")
                } else {
                    Log.e("DisplayTextActivity", "Erreur lors de l'enregistrement de la consultation: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception lors de l'enregistrement de la consultation", e)
            }
        }
    }

}


