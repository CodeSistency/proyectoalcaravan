import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun UpdatedProfile(navController: NavHostController, string: String?) {
 Column {
  Text(text = "Profile ${string}")
  Button(onClick = { navController.popBackStack()}) {
   Text(text = "Atras")
  }
 }
 }
