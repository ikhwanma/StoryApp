package ikhwan.dicoding.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import ikhwan.dicoding.storyapp.databinding.ActivityDetailBinding
import ikhwan.dicoding.storyapp.model.Story
import ikhwan.dicoding.storyapp.ui.home.HomeActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.extras!!.getParcelable<Story>(HomeActivity.DATA_KEY) as Story

        with(binding){
            Glide.with(this@DetailActivity).load(story.photoUrl).into(ivStory)
            tvName.text = story.name
            tvDeskripsi.text = story.description
        }
    }
}