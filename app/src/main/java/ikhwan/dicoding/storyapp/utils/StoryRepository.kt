package ikhwan.dicoding.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import ikhwan.dicoding.storyapp.data.StoryPagingSource
import ikhwan.dicoding.storyapp.model.StoriesResponse
import ikhwan.dicoding.storyapp.model.Story
import ikhwan.dicoding.storyapp.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Part

class StoryRepository(private val apiService: ApiService) {
    fun getAllStories(auth: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, auth)
            }
        ).liveData
    }

    fun getAllStory(token: String) = apiService.getAllStories(auth = token, location = 1)

    fun uploadImage(
        auth: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) = apiService.uploadImage(auth, file, description)
}