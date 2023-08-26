package ikhwan.dicoding.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.dicoding.storyapp.ui.detail.DetailActivity
import ikhwan.dicoding.storyapp.ui.login.MainActivity
import ikhwan.dicoding.storyapp.R
import ikhwan.dicoding.storyapp.databinding.ActivityHomeBinding
import ikhwan.dicoding.storyapp.model.StoriesResponse
import ikhwan.dicoding.storyapp.network.ApiConfig
import ikhwan.dicoding.storyapp.ui.add_story.AddStoryActivity
import ikhwan.dicoding.storyapp.ui.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(
            MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE
        )

        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, "")!!

        val storyAdapter = StoryAdapter()
        storyAdapter.onItemClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DATA_KEY, it)
            startActivity(intent)
        }
        binding.rvStory.adapter = storyAdapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()

        viewModel.listStories("Bearer $token").observe(this){
            storyAdapter.submitData(lifecycle, it)
        }

        binding.fabAdd.setOnClickListener(this)
        binding.fabMaps.setOnClickListener(this)
        binding.fabLogout.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fab_add -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
            }
            R.id.fab_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.fab_logout -> {
                sharedPreferences.edit().clear().apply()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val DATA_KEY = "data_key"
    }
}