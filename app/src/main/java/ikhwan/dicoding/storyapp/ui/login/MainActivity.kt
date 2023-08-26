package ikhwan.dicoding.storyapp.ui.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ikhwan.dicoding.storyapp.R
import ikhwan.dicoding.storyapp.ui.register.RegisterActivity
import ikhwan.dicoding.storyapp.databinding.ActivityMainBinding
import ikhwan.dicoding.storyapp.model.LoginBody
import ikhwan.dicoding.storyapp.model.LoginResponse
import ikhwan.dicoding.storyapp.network.ApiConfig
import ikhwan.dicoding.storyapp.ui.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(
            PREFERENCES_KEY, Context.MODE_PRIVATE
        )

        val token = sharedPreferences.getString(TOKEN_KEY, "")

        if (token != ""){
            startActivity(Intent(this, HomeActivity::class.java))
        }

        playAnimation()

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.etEmail, View.TRANSLATION_X, -30f, 0f).apply {
            duration = 2000
        }.start()
        ObjectAnimator.ofFloat(binding.etPassword, View.TRANSLATION_X, 30f, 0f).apply {
            duration = 2000
        }.start()
        ObjectAnimator.ofFloat(binding.btnLogin, View.TRANSLATION_X, -30f, 0f).apply {
            duration = 2000
        }.start()
        ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, 0f, 10f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.btn_login -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                with(binding) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val loginBody = LoginBody(email, password)
                    viewModel.login(loginBody)
                    viewModel.loginResponse.observe(this@MainActivity){
                        if (it != null){

                        }
                        if (it.error){
                            Toast.makeText(
                                this@MainActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@observe
                        }
                        if (it.loginResult != null){
                            val token = it.loginResult.token
                            Toast.makeText(
                                this@MainActivity,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        }
                    }
                }

            }
        }
    }

    companion object {
        const val PREFERENCES_KEY = "preferences_key"
        const val TOKEN_KEY = "token_key"
    }
}