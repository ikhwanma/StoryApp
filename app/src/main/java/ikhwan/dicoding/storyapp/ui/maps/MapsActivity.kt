package ikhwan.dicoding.storyapp.ui.maps

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ikhwan.dicoding.storyapp.R
import ikhwan.dicoding.storyapp.databinding.ActivityMapsBinding
import ikhwan.dicoding.storyapp.ui.login.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModel()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(
            MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, "")!!

        viewModel.getAllStory("Bearer $token")
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.responseList.observe(this){
            val list = it.listStory
            val listFirst = LatLng(list[0].lat, list[0].lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(listFirst))
            for(d in list){
                val pos = LatLng(d.lat, d.lon)
                mMap.addMarker(MarkerOptions().position(pos).title(d.name))
            }
        }
    }
}