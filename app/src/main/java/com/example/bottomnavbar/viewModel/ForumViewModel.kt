package com.example.bottomnavbar.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bottomnavbar.model.Forum
import com.example.bottomnavbar.repository.ForumRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class ForumViewModel : ViewModel() {

    var response: Response<String?>? = null

    private val _addForumResult = MutableLiveData<Response<String>>()
    val addEventResult: LiveData<Response<String>> = _addForumResult

    //-----get All events

    private val repository = ForumRepository()

    private val _deleteForumResult = MutableLiveData<Response<Unit>>()
    val deleteForumResult: LiveData<Response<Unit>> = _deleteForumResult

    private val _isforumDeleted = MutableLiveData<Boolean>()
    val deleteForumtResult: LiveData<Boolean> = _isforumDeleted


    val forums = liveData {
        val eventData = repository.getAllForums()
        emit(eventData ?: emptyList())
    }

    //------add event
    fun addForum(imageUri: Uri?, title: String, description: String) {

        // Vérifiez si l'URI de l'image est non nul
        if (imageUri != null) {
            // Convertissez l'URI en File
            val imageFile = File(imageUri.path)

            // Créez une instance de MultipartBody.Part pour l'image
            val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
            val imagePart =
                MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

            // Convertissez les titres et descriptions en RequestBody
            val titleRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
            val descriptionRequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), description)

            viewModelScope.launch {
                val response =
                    repository.addForum(imagePart, titleRequestBody, descriptionRequestBody)
                _addForumResult.postValue(response)
            }


            // LiveData pour stocker la liste des forums
            val _forumList = MutableLiveData<List<Forum>>()

            // Fonction pour mettre à jour la liste des forums
            fun updateForumList(newForumList: List<Forum>) {
                _forumList.value = newForumList
            }

        }
    }

    suspend fun deleteForum(forumId: String) {
        try {
            val result = repository.deleteForum(forumId)
            _isforumDeleted.value = true
        } catch (e: Exception) {
            _isforumDeleted.value = false
        }
    }
}