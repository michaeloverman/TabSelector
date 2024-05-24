package digital.overman.tabselectorexperiments

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import digital.overman.tabselectorexperiments.ui.theme.TabSelectorExperimentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TabSelectorExperimentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        ItemContainer(
                            listOf(
                                TabItem("All") {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "All clicked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                TabItem("Some") {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Some clicked",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                                TabItem("Other") {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Other clicked",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                                TabItem("Most") {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Most Clicked",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                },
                            ),
                        )
                    }
                }
            }
        }
    }
}
