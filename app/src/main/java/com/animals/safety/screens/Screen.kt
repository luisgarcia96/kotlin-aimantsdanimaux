package com.animals.safety.screens

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  data object Home : Screen("home")

  data object AnimalDetails : Screen(
    route = "animalDetails/{animalId}",
    navArguments = listOf(navArgument("animalId") {
      type = NavType.StringType
    })
  ) {
    fun createRoute(animalId: String) = "animalDetails/$animalId"
  }

  data object CreateAnimal : Screen("createAnimal")
}
