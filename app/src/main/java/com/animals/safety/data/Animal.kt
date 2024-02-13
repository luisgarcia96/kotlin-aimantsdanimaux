package com.animals.safety.data

import java.io.Serializable
import java.util.UUID

data class Animal(
  val id: UUID,
  val name: String,
  val breed: Breed,
  val age: Int,
  val weight: Float,
  val height: Float
) : Serializable
