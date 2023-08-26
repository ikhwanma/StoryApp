package ikhwan.dicoding.storyapp.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.dicoding.storyapp.model.StoriesResponse
import ikhwan.dicoding.storyapp.utils.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(
    private val repository: StoryRepository
) : ViewModel() {
    val responseList = MutableLiveData<StoriesResponse>()
    val errMessage = MutableLiveData<String>()

    fun getAllStory(auth: String) =
        repository.getAllStory(auth).enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                responseList.postValue(response.body()!!)
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                errMessage.postValue("Gagal")
            }

        })
}