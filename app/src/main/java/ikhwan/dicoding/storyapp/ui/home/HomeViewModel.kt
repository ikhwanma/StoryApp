package ikhwan.dicoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ikhwan.dicoding.storyapp.model.StoriesResponse
import ikhwan.dicoding.storyapp.model.Story
import ikhwan.dicoding.storyapp.utils.StoryRepository

class HomeViewModel(
    private val repository: StoryRepository
): ViewModel() {

    fun listStories(auth: String): LiveData<PagingData<Story>> =
        repository.getAllStories(auth).cachedIn(viewModelScope)
}