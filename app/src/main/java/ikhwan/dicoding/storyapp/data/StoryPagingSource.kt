package ikhwan.dicoding.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ikhwan.dicoding.storyapp.model.StoriesResponse
import ikhwan.dicoding.storyapp.model.Story
import ikhwan.dicoding.storyapp.network.ApiService
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryPagingSource(
    private val apiService: ApiService,
    private val auth: String
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = mutableListOf<Story>()
            apiService.getAllStories(auth = auth, page = position, size = params.loadSize)
                .enqueue(object : Callback<StoriesResponse> {
                    override fun onResponse(
                        call: Call<StoriesResponse>,
                        response: Response<StoriesResponse>
                    ) {
                        val body = response.body()!!
                        responseData.addAll(body.listStory)
                    }

                    override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                        Log.d("Fail", "fail")
                    }

                })
            delay(3000)
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}