package com.animals.safety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.animals.safety.data.AnimalData
import com.animals.safety.screens.AnimalDetailsScreen
import com.animals.safety.screens.CreateAnimalScreen
import com.animals.safety.screens.HomeScreen
import com.animals.safety.screens.Screen
import com.animals.safety.ui.theme.AimantsDanimauxTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val navController = rememberNavController()

      AimantsDanimauxTheme {
        AimantsDanimauxNavHost(navHostController = navController)
      }
    }
  }

}

@Composable
fun AimantsDanimauxNavHost(navHostController: NavHostController) {
  NavHost(
    navController = navHostController,
    startDestination = Screen.Home.route
  ) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        onAnimalClick = {
          navHostController.navigate(
            Screen.AnimalDetails.createRoute(animalId = it.id.toString())
          )
        },
        onFABClick = {
          navHostController.navigate(Screen.CreateAnimal.createRoute(null))
        }
      )
    }
    composable(
      route = Screen.AnimalDetails.route,
      arguments = Screen.AnimalDetails.navArguments
    ) {
      AnimalDetailsScreen(
        animal = AnimalData.findAnimalById(it.arguments?.getString("animalId") ?: ""),
        onBackClick = { navHostController.navigateUp() },
        navController = navHostController
      )
    }
    composable(
      route = Screen.CreateAnimal.route,
      arguments = Screen.CreateAnimal.navArguments
    ) { backStackEntry ->
      val animalId = backStackEntry.arguments?.getString("animalId")
      val animal = animalId?.let { AnimalData.findAnimalById(it) }

      CreateAnimalScreen(
        animal = animal,
        onBackClick = { navHostController.navigateUp() },
        onSaveClick = { navHostController.navigateUp() }
      )
    }
  }
}

