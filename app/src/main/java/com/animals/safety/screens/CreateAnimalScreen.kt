package com.animals.safety.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.animals.safety.R
import com.animals.safety.data.Animal
import com.animals.safety.data.AnimalData
import com.animals.safety.data.Breed
import com.animals.safety.ui.theme.AimantsDanimauxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnimalScreen(
  modifier: Modifier = Modifier,
  animal: Animal?,
  onBackClick: () -> Unit,
  onSaveClick:() -> Unit
) {
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  //TODO: à compléter
  val name = remember { mutableStateOf("") }
  val breed = remember { mutableStateOf(Breed.entries[0]) }
  val age = remember { mutableStateOf("") }
  val weight = remember { mutableStateOf("") }
  val height = remember { mutableStateOf("") }

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(id = R.string.create_fragment_label))
        },
        navigationIcon = {
          IconButton(onClick = {
            onBackClick()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Go back"
            )
          }
        }
      )
    },
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    },
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = {
          if (verifyAndCreateAnimal(animal, name.value, breed.value, age.value, weight.value, height.value, snackbarHostState, scope)) {
            onSaveClick()
          }
        }
      ) {
        Text(
          text = stringResource(id = R.string.action_save)
        )
      }
    }
  ) { contentPadding ->
    CreateAnimal(
      modifier = Modifier.padding(contentPadding),
      name = name,
      breed = breed,
      age = age,
      weight = weight,
      height = height
    )
  }
}

fun verifyAndCreateAnimal(
  animal: Animal?,
  name: String,
  breed: Breed,
  age: String,
  weight: String,
  height: String,
  snackbarHostState: SnackbarHostState,
  scope: CoroutineScope
): Boolean
{
  if (name.isBlank()) {
    scope.launch {
      snackbarHostState.showSnackbar("The name must not be empty")
    }

    return false;
  }

  val animalAge: Int;
  try {
    animalAge = age.toInt()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar("The age is not valid")
    }

    return false;
  }

  val animalWeight: Float;
  try {
    animalWeight = weight.toFloat()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar("The weight is not valid")
    }

    return false;
  }

  val animalHeight: Float;
  try {
    animalHeight = height.toFloat()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar("The height is not valid")
    }

    return false;
  }

  animal?.let {
    AnimalData.animals.remove(it)
  }

  AnimalData.animals.add(
    Animal(
      animal?.id ?: UUID.randomUUID(),
      name,
      breed,
      animalAge,
      animalWeight,
      animalHeight
    )
  )

  return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAnimal(
  modifier: Modifier = Modifier,
  name: MutableState<String>,
  age: MutableState<String>,
  weight: MutableState<String>,
  height: MutableState<String>,
  breed: MutableState<Breed>
) {
  val scrollState = rememberScrollState()
  var isExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
      .fillMaxSize()
      .verticalScroll(scrollState)
  ) {
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = name.value,
      onValueChange = { name.value = it },
      label = { Text(stringResource(id = R.string.hint_name)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      singleLine = true
    )
    ExposedDropdownMenuBox(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      expanded = isExpanded,
      onExpandedChange = { isExpanded = it }
    ) {
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .menuAnchor(),
        value = stringResource(id = breed.value.translatedName),
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
        label = { Text(text = stringResource(id = R.string.hint_breed)) },
      )
      ExposedDropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false }
      ) {
        Breed.entries.forEach {
          DropdownMenuItem(
            text = { Text(text = stringResource(id = it.translatedName)) },
            onClick = {
              breed.value = it
              isExpanded = false
            }
          )
        }
      }
    }
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = age.value,
      onValueChange = { age.value = it },
      label = { Text(stringResource(id = R.string.hint_age)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
      singleLine = true
    )
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = weight.value,
      onValueChange = { weight.value = it },
      label = { Text(stringResource(id = R.string.hint_weight)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
      singleLine = true
    )
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = height.value,
      onValueChange = { height.value = it },
      label = { Text(stringResource(id = R.string.hint_height)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
      singleLine = true
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun CreateAnimalPreview() {
  AimantsDanimauxTheme(dynamicColor = false) {
    val name = remember { mutableStateOf("Milou") }
    val breed = remember { mutableStateOf(Breed.entries[0]) }
    val age = remember { mutableStateOf("6") }
    val weight = remember { mutableStateOf("473.6") }
    val height = remember { mutableStateOf("14.7") }

    CreateAnimal(
      name = name,
      age = age,
      weight = weight,
      height = height,
      breed = breed
    )
  }
}