package com.example.igdp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.igdp.ui.theme.IGDPTheme


class GamePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IGDPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameDetails(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GameDetails(modifier: Modifier = Modifier) {

}


@Preview(showBackground = false, showSystemUi = true)
@Composable
private fun GameDetailsPreview() {
    IGDPTheme {
        GameDetails()
    }
}