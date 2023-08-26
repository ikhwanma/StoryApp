package ikhwan.dicoding.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import ikhwan.dicoding.storyapp.data.StoryPagingSourceTest
import ikhwan.dicoding.storyapp.model.Story
import ikhwan.dicoding.storyapp.utils.DataDummy
import ikhwan.dicoding.storyapp.utils.MainCoroutinesRule
import ikhwan.dicoding.storyapp.utils.StoryRepository
import ikhwan.dicoding.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyStory = DataDummy.generateDummyStory()
    private val auth = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTk3cmFpWGV4WmQ3VkxyQ1ciLCJpYXQiOjE2ODA0Mjk4MDB9.ScpmCHLw4mYPaXUOwLaZn4avGnDc74yTW0rjUhE6rpI"

    @Before
    fun setUp(){
        homeViewModel = HomeViewModel(storyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get story with pagination successfully`() = runTest{
        val observer = Observer<PagingData<Story>> {}
        try {
            val data = StoryPagingSourceTest.snapshot(dummyStory)
            val expectedStory = MutableLiveData<PagingData<Story>>()
            expectedStory.value = data
            `when`(storyRepository.getAllStories(auth)).thenReturn(expectedStory)
            val actualStory = homeViewModel.listStories(auth).getOrAwaitValue()
            assertNotNull(actualStory)

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                workerDispatcher = Dispatchers.Main,
                updateCallback = ikhwan.dicoding.storyapp.utils.ListUpdateCallback.noopListUpdateCallback
            )

            differ.submitData(actualStory)

            verify(storyRepository).getAllStories(auth)
            assertNotNull(differ.snapshot())
            assertEquals(dummyStory.size, differ.snapshot().size)
            assertEquals(dummyStory[0], differ.snapshot()[0])
        }finally {
            homeViewModel.listStories(auth).removeObserver(observer)
        }

    }

    @Test
    fun `get story with pagination failed`() = runTest {
        val dummyStory = emptyList<Story>()
        val data = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        `when`(storyRepository.getAllStories("unauthorized")).thenReturn(expectedStory)
        val actualStory = homeViewModel.listStories("unauthorized").getOrAwaitValue()
        verify(storyRepository).getAllStories("unauthorized")

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            workerDispatcher = Dispatchers.Main,
            updateCallback = ikhwan.dicoding.storyapp.utils.ListUpdateCallback.noopListUpdateCallback
        )
        differ.submitData(actualStory)
        assertEquals(0, differ.snapshot().size)
    }
}