package com.animals.safety.data

import java.util.UUID

object AnimalData {
  
  private val milou = Animal(UUID.randomUUID(),"Milou", Breed.DOG, 6, 23.2f, 42.4f)
  
  private val rantanplan = Animal(UUID.randomUUID(), "Rantanplan", Breed.DOG, 8, 28.1f, 38.4f)
  
  private val garfield = Animal(UUID.randomUUID(), "Garfield", Breed.CAT, 12, 5.4f, 21.3f)
  
  private val marguerite = Animal(UUID.randomUUID(), "Marguerite", Breed.COW, 3, 668.3f, 148f)
  
  val animals = mutableListOf(milou, rantanplan, garfield, marguerite)

  fun findAnimalById(animalId: String) : Animal =
    animals.first { it.id.toString() == animalId }

}

