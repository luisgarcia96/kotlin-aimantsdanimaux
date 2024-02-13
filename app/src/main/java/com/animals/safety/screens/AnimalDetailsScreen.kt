package com.animals.safety.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.animals.safety.R
import com.animals.safety.data.Animal
import com.animals.safety.data.Breed
import com.animals.safety.ui.theme.AimantsDanimauxTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(
  modifier: Modifier = Modifier,
  animal: Animal,
  onBackClick: () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(id = R.string.details_fragment_label))
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
        },
      )
    },
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        ExtendedFloatingActionButton(
          onClick = {
            //TODO
          },
          icon = { Icon(Icons.Filled.Edit, "Edit") },
          text = { Text(text = stringResource(id = R.string.description_button_edit)) },
        )
        ExtendedFloatingActionButton(
          onClick = {
            //TODO
          },
          contentColor = Color.White,
          containerColor = Color.Red,
          icon = {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Delete"
            )
         },
          text = {
            Text(
              text = stringResource(id = R.string.description_button_delete),
            )
           },
        )
      }
    }
  ) { contentPadding ->
    AnimalDetails(
      modifier = modifier.padding(contentPadding),
      animal = animal,
    )
  }
}

@Composable
private fun AnimalDetails(
  modifier: Modifier = Modifier,
  animal: Animal,
)
{
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(modifier = Modifier.aspectRatio(3f / 2f)) {
      Image(
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop,
        painter = painterResource(animal.breed.cover),
        contentDescription = "cover"
      )
      Text(
        modifier = Modifier
          .padding(16.dp)
          .align(Alignment.BottomStart),
        text = animal.name,
        color = Color.White,
        style = MaterialTheme.typography.titleLarge
      )
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 32.dp),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      Property(
        drawableRes = R.drawable.ic_age,
        text = stringResource(id = R.string.value_age, animal.age.toString())
      )
      Property(
        drawableRes = R.drawable.ic_weight,
        text = stringResource(id = R.string.value_weight, animal.weight.toString())
      )
    }
    Property(
      modifier = Modifier.padding(top = 32.dp),
      drawableRes = R.drawable.ic_height,
      text = stringResource(id = R.string.value_height, animal.height.toString())
    )
  }
}

@Composable
private fun Property(
  modifier: Modifier = Modifier,
  @DrawableRes drawableRes: Int,
  text: String
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      modifier = Modifier
        .padding(16.dp)
        .width(54.dp)
        .height(54.dp),
      tint = MaterialTheme.colorScheme.onSurface,
      painter = painterResource(drawableRes),
      contentDescription = text
    )
    Text(
      text = text,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurface,
      style = MaterialTheme.typography.bodyMedium
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PropertyPreview() {
  AimantsDanimauxTheme(dynamicColor = false) {
    Property(
      drawableRes = R.drawable.ic_age,
      text = "6 ans"
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AnimalDetailsPreview() {
  AimantsDanimauxTheme(dynamicColor = false) {
    AnimalDetails(
      animal = Animal(UUID.randomUUID(),"Milou", Breed.DOG, 6, 23.2f, 42.4f),
    )
  }
}
