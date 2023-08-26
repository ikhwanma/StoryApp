package ikhwan.dicoding.storyapp.utils

import ikhwan.dicoding.storyapp.model.Story

object DataDummy {
    fun generateDummyStory(): List<Story>{
        val storyList = ArrayList<Story>()
        for (i in 0..10){
            val story = Story(
                "2023-04-01T18:20:28.747Z",
                "tes lg",
                i.toString(),
                -6.2872986,
                107.0120745,
                "Namaku Siapa",
                "https://story-api.dicoding.dev/images/stories/photos-1680373228745_vDhRubgO.jpg"
            )
            storyList.add(story)
        }
        return storyList
    }
}