package ikhwan.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ikhwan.dicoding.storyapp.model.Story
import org.junit.Assert.*

import org.junit.Test

class StoryPagingSourceTest: PagingSource<Int, Story>() {

    companion object{
        fun snapshot(item: List<Story>): PagingData<Story>{
            return PagingData.from(item)
        }
    }

    @Test
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return 0
    }

    @Test
    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Story> {
        return PagingSource.LoadResult.Page(emptyList(),0,1)
    }
}