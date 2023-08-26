package ikhwan.dicoding.storyapp.ui.add_story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.dicoding.storyapp.model.FileUploadResponse
import ikhwan.dicoding.storyapp.network.ApiService
import ikhwan.dicoding.storyapp.utils.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(
    private val repository: StoryRepository
) : ViewModel() {
    val responseFileUpload = MutableLiveData<FileUploadResponse>()
    val errMessage = MutableLiveData<String>()

    fun uploadImage(
        auth: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) = repository.uploadImage(auth, file, description).enqueue(object :
        Callback<FileUploadResponse> {
        override fun onResponse(
            call: Call<FileUploadResponse>,
            response: Response<FileUploadResponse>
        ) {
            val body = response.body()!!
            responseFileUpload.postValue(body)
        }

        override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
            errMessage.postValue("gagal")
        }

    })

}