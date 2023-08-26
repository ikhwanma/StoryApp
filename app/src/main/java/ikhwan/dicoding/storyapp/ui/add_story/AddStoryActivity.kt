package ikhwan.dicoding.storyapp.ui.add_story

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import ikhwan.dicoding.storyapp.*
import ikhwan.dicoding.storyapp.databinding.ActivityAddStoryBinding
import ikhwan.dicoding.storyapp.ui.home.HomeActivity
import ikhwan.dicoding.storyapp.ui.login.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var token: String
    private val viewModel: AddStoryViewModel by viewModel()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapat permission", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val mFile = File(currentPhotoPath)
        val reducedFile = reduceFileImage(mFile)
        getFile = reducedFile
        val result = rotateBitmap(BitmapFactory.decodeFile(reducedFile.path), true)
        binding.ivImage.setImageBitmap(result)
        binding.ivPost.visibility = View.GONE
    }


    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(
            MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        token = sharedPreferences.getString(MainActivity.TOKEN_KEY, "")!!

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cvImg.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            createCustomTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "ikhwan.dicoding.storyapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
        binding.btnAdd.setOnClickListener {
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            uploadStory()
        }
    }

    private fun uploadStory() {
        val description = binding.etDescription.text.toString()
        if (getFile != null && description != "") {
            val file = reduceFileImage(getFile as File)
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            viewModel.uploadImage("Bearer $token", imageMultiPart, requestDescription)
            viewModel.responseFileUpload.observe(this){ responseBody ->
                if (responseBody != null && !responseBody.error) {
                    Toast.makeText(this@AddStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddStoryActivity, HomeActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@AddStoryActivity, responseBody!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.errMessage.observe(this){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}