package com.animals.safety.screens

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  //TODO: à compléter
  val name = rememberSaveable { mutableStateOf(animal?.name ?: "") }
  val breed = rememberSaveable { mutableStateOf(animal?.breed ?: Breed.entries[0]) }
  val age = rememberSaveable { mutableStateOf(animal?.age?.toString() ?: "") }
  val weight = rememberSaveable { mutableStateOf(animal?.weight?.toString() ?: "") }
  val height = rememberSaveable { mutableStateOf(animal?.height?.toString() ?: "") }

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          if (animal == null) {
            Text(stringResource(id = R.string.create_fragment_label))
          } else {
            Text(stringResource(id = R.string.description_button_edit))
          }
        },
        navigationIcon = {
          IconButton(onClick = {
            onBackClick()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(id = R.string.contentDescription_go_back)
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
          if (verifyAndCreateAnimal(animal, name.value, breed.value, age.value, weight.value, height.value, snackbarHostState, scope, context)) {
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
      name = name.value,
      onNameChanged = { name.value = it },
      breed = breed.value,
      onBreedChanged = { breed.value = it },
      age = age.value,
      onAgeChanged = { age.value = it },
      weight = weight.value,
      onWeightChanged = { weight.value = it },
      height = height.value,
      onHeightChanged = { height.value = it }
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
  scope: CoroutineScope,
  context: Context
): Boolean
{
  if (name.isBlank()) {
    scope.launch {
      snackbarHostState.showSnackbar(context.getString(R.string.issue_name_empty))
    }

    return false;
  }

  val animalAge: Int;
  try {
    animalAge = age.toInt()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar(context.getString(R.string.issue_invalid_age))
    }

    return false;
  }

  val animalWeight: Float;
  try {
    animalWeight = weight.toFloat()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar(context.getString(R.string.issue_invalid_weight))
    }

    return false;
  }

  val animalHeight: Float;
  try {
    animalHeight = height.toFloat()
  } catch (e: NumberFormatException) {
    scope.launch {
      snackbarHostState.showSnackbar(context.getString(R.string.issue_invalid_height))
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
  name: String,
  onNameChanged: (String) -> Unit,
  age: String,
  onAgeChanged: (String) -> Unit,
  weight: String,
  onWeightChanged: (String) -> Unit,
  height: String,
  onHeightChanged: (String) -> Unit,
  breed: Breed,
  onBreedChanged: (Breed) -> Unit
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
      value = name,
      onValueChange = { onNameChanged(it) },
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
        value = stringResource(id = breed.translatedName),
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
              onBreedChanged(it)
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
      value = age,
      onValueChange = { onAgeChanged(it) },
      label = { Text(stringResource(id = R.string.hint_age)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
      singleLine = true
    )
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = weight,
      onValueChange = { onWeightChanged(it) },
      label = { Text(stringResource(id = R.string.hint_weight)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
      singleLine = true
    )
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = height,
      onValueChange = { onHeightChanged(it) },
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
    CreateAnimal(
      name = "Milou",
      onNameChanged = { },
      age = "6",
      onAgeChanged = { },
      weight = "473.6",
      onWeightChanged = { },
      height = "14.7",
      onHeightChanged = { },
      breed = Breed.entries[0],
      onBreedChanged = { }
    )
  }
}