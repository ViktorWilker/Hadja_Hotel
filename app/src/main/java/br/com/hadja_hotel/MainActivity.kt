package br.com.hadja_hotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.hadja_hotel.navigation.NavGraph
import br.com.hadja_hotel.ui.screens.LoginScreen
import br.com.hadja_hotel.ui.theme.Hadja_HotelTheme
import br.com.hadja_hotel.viewmodel.HotelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hadja_HotelTheme {
                NavGraph()
            }
        }
    }
}
