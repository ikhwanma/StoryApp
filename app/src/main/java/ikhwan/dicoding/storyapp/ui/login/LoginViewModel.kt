package ikhwan.dicoding.storyapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.dicoding.storyapp.model.LoginBody
import ikhwan.dicoding.storyapp.model.LoginResponse
import ikhwan.dicoding.storyapp.utils.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val repository: AuthRepository
): ViewModel() {
    val loginResponse = MutableLiveData<LoginResponse>()
    val failMessage = MutableLiveData<String>()

    fun login(loginBody: LoginBody){
        repository.login(loginBody).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()!!
                loginResponse.postValue(responseBody)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                failMessage.postValue("Gagal")
            }

        })
    }
}