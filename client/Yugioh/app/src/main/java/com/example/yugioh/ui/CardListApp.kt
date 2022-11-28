package com.example.yugioh.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.yugioh.R
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.yugioh.ui.screens.CardsViewModel
import com.example.yugioh.ui.screens.HomeScreen
import androidx.navigation.compose.rememberNavController
import com.example.yugioh.ui.screens.CreateCardScreen

enum class CardsScreen() {
    List,
    Create
}

@ExperimentalAnimationApi
@Composable
fun CardsListApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val cardsViewModel: CardsViewModel = viewModel(factory = CardsViewModel.Factory)
    var searchInput by remember { mutableStateOf("") }
    val onSearchInputChange: (String) -> Unit = {
        searchInput = it
        cardsViewModel.setSearchText(searchInput)
    }
    val currentRoute = backStackEntry?.destination?.route

    val currentScreen = CardsScreen.valueOf(
        currentRoute ?: CardsScreen.List.name
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { CardsTopAppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            searchInput = searchInput,
            onSearchInputChange = onSearchInputChange,
            onMenuClicked = {
                navController.navigate(CardsScreen.Create.name)
            },
            navigateUp = { navController.navigateUp() }
        ) },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = CardsScreen.List.name,
                modifier = Modifier.padding(it)
            ) {
                composable(route = CardsScreen.List.name) {
                    HomeScreen(
                        cardsUiState = cardsViewModel.cardsUiState,
                        cardsList = cardsViewModel.items,
                        loadMoreItems = cardsViewModel.getMoreCards
                    )
                }

                composable(route = CardsScreen.Create.name) {
                    CreateCardScreen(
                        cardsViewModel.postCard
                    )
                }
            }
        }

    }
}

@Composable
fun SearchField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) ->  Unit,
    modifier: Modifier = Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun CardsTopAppBar(
    currentScreen: CardsScreen,
    canNavigateBack: Boolean,
    searchInput: String,
    onSearchInputChange: (String) -> Unit,
    onMenuClicked: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current

    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!canNavigateBack) {
            Image(
                painter = painterResource(id = R.drawable.card_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clickable { onMenuClicked() }
            )
        } else {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (currentScreen == CardsScreen.List) {
            SearchField(
                value = searchInput,
                onValueChange = onSearchInputChange,
                label = R.string.search_field,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions( onDone = { focusManager.clearFocus() } )
            )
        } else if (currentScreen == CardsScreen.Create) {
            Text(text = stringResource(id = R.string.create_card), style = MaterialTheme.typography.h1)
        }
    }
}
