package ikhwan.dicoding.storyapp.model

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult?,
    val message: String
)