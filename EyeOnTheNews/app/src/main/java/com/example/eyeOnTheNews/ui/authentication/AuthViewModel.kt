package com.example.eyeOnTheNews.ui.authentication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class userAuthState(
    val email: String = "",
    val password: String = "",
    val currentUser: String? = null,
    val loginResult: String? = null,
    var registerResult: String? = null

)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(userAuthState())
    val uiState: StateFlow<userAuthState> = _uiState.asStateFlow()

    fun registerUser(email: String, password: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value.registerResult =
                        AuthResult.PARCELABLE_WRITE_RETURN_VALUE.toString()
                } else {
                    _uiState.value.registerResult =
                        AuthResult.PARCELABLE_WRITE_RETURN_VALUE.toString()
                }
            }
    }
}
