package ikhwan.dicoding.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import ikhwan.dicoding.storyapp.R
import ikhwan.dicoding.storyapp.databinding.ActivityRegisterBinding
import ikhwan.dicoding.storyapp.model.FileUploadResponse
import ikhwan.dicoding.storyapp.model.RegisterBody
import ikhwan.dicoding.storyapp.network.ApiConfig
import ikhwan.dicoding.storyapp.ui.login.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            var isEmailErr = true
            var isPasswordErr = true
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etEmail.error.isNullOrEmpty()) isEmailErr = false
                }

                override fun afterTextChanged(p0: Editable?) {
                    setButtonEnabled(isEmailErr, isPasswordErr)
                }

            })
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etPassword.error.isNullOrEmpty()) isPasswordErr = false
                    if (etPassword.text!!.length > 7) isPasswordErr = false
                }

                override fun afterTextChanged(p0: Editable?) {
                    setButtonEnabled(isEmailErr, isPasswordErr)
                }

            })
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun setButtonEnabled(isEmailErr: Boolean, isPasswordErr: Boolean) {
        binding.btnRegister.isEnabled = !isEmailErr && !isPasswordErr
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_register -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                with(binding) {
                    val name = etName.text.toString()
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    val registerBody = RegisterBody(name, email, password)

                    if (name != "" && email != "" && password != "") {
                        viewModel.register(registerBody)
                        viewModel.registerResponse.observe(this@RegisterActivity){ responseBody ->
                            if (!responseBody.error) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    responseBody.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        MainActivity::class.java
                                    )
                                )
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    responseBody.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        viewModel.errMessage.observe(this@RegisterActivity){
                            Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }
}