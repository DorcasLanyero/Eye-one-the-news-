package com.example.eyeOnTheNews.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import com.example.eyeOnTheNews.ui.navigation.EyeOnTheNewsNavGraph
import com.example.eyeOnTheNews.ui.theme.EyeOnTheNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EyeOnTheNewsTheme(false, true) {
                Surface {
                    EyeOnTheNewsNavGraph()
                }
            }
        }
    }
}




