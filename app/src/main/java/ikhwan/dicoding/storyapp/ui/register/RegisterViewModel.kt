package ikhwan.dicoding.storyapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.dicoding.storyapp.model.FileUploadResponse
import ikhwan.dicoding.storyapp.model.RegisterBody
import ikhwan.dicoding.storyapp.network.ApiService
import ikhwan.dicoding.storyapp.utils.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val repository: AuthRepository
): ViewModel() {
    val registerResponse = MutableLiveData<FileUploadResponse>()
    val errMessage = MutableLiveData<String>()

    fun register(registerBody: RegisterBody){
        repository.register(registerBody).enqueue(object : Callback<FileUploadResponse>{
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                val data = response.body()!!
                registerResponse.postValue(data)
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                errMessage.postValue("Gagal")
            }

        })
    }
}