package ikhwan.dicoding.storyapp.utils

import ikhwan.dicoding.storyapp.model.LoginBody
import ikhwan.dicoding.storyapp.model.RegisterBody
import ikhwan.dicoding.storyapp.network.ApiService

class AuthRepository(private val apiService: ApiService) {
    fun login(loginBody: LoginBody) = apiService.login(loginBody)
    fun register(registerBody: RegisterBody) = apiService.register(registerBody)
}