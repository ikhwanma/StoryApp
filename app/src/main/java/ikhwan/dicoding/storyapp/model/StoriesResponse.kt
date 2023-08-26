package ikhwan.dicoding.storyapp.model

data class StoriesResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)