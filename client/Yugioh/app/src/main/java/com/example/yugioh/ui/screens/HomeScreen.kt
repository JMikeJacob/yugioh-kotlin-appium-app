package com.example.yugioh.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.yugioh.R
import com.example.yugioh.model.CardModel
import kotlin.reflect.KFunction3

private const val TAG = "cardsHomeScreen"

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    cardsUiState: CardsUiState,
    cardsList: List<CardModel>,
    loadMoreItems: () -> Unit,
    modifier: Modifier = Modifier
) {
//    when(cardsUiState) {
//        is CardsUiState.Loading -> LoadingScreen()
//        is CardsUiState.Success -> CardsScreen(cardsList = cardsList, loadMoreItems = loadMoreItems)
//        is CardsUiState.Error -> ErrorScreen({ Log.d(TAG, "error fetching items") })
//    }
    CardsScreen(cardsList = cardsList, loadMoreItems = loadMoreItems)
}

@ExperimentalAnimationApi
@Composable
fun CardsScreen(cardsList: List<CardModel>, loadMoreItems: () -> Unit, modifier: Modifier = Modifier) {
    var isPhotoShowing by remember { mutableStateOf(false) }
    var imageShownUrl by remember { mutableStateOf("") }
    val showPhoto: ((card: CardModel) -> Unit) = {
        isPhotoShowing = true
        imageShownUrl = it.imgUri ?: ""
    }

    val hidePhoto: (() -> Unit) = {
        isPhotoShowing = false
    }

    val listState = rememberLazyListState()

    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)) {

        LazyColumn (
            state = listState,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()) {
            items(cardsList) {
                CardItem(card = it, showPhoto)
            }
        }

        CardPhoto(imageUrl = imageShownUrl, isPhotoShowing, hidePhoto)
    }

    listState.OnBottomReached {
        Log.d(TAG, "hit bottom")
        loadMoreItems()
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            Log.d(TAG, "index: ${lastVisibleItem.index}, count: ${layoutInfo.totalItemsCount}")
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}

@ExperimentalAnimationApi
@Composable
fun CardPhoto(imageUrl: String, visible: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .background(Color.Black.copy(alpha = 0.6f))
        .clickable { onClick.invoke() }) {

        AnimatedVisibility(visible = visible,
            enter = slideInVertically(initialOffsetY = { -40 })
                    + expandVertically(expandFrom = Alignment.Top)
                    + scaleIn(transformOrigin = TransformOrigin(0.5f, 0f))
                    + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = imageUrl,
                modifier = modifier
                    .fillMaxSize()
            )
        }

    }
}

@Composable
fun CardItem(card: CardModel, onImageClick: (card: CardModel) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = modifier.padding(8.dp), elevation = 4.dp) {
        Column(
            modifier = modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                CardIcon(card, onImageClick)
                CardInformation(card.name, card.type)
                Spacer(Modifier.weight(1f))
                CardItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }

            if (expanded) {
                CardDescription(description = card.description, atk = card.atk, def = card.def)
            }
        }
    }
}

@Composable
fun CardInformation(name: String, type: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.h2,
            modifier = modifier.padding(top = 8.dp)
        )

        Text(
            text = type,
            style = MaterialTheme.typography.body1,
            modifier = modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CardIcon(card: CardModel, onClick: (card: CardModel) -> Unit, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(card.imgSmallUri)
            .build(),
        contentDescription = card.name,
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
            .clickable { onClick.invoke(card) }
    )

}


@Composable
private fun CardItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        if (!expanded) {
            Icon(
                imageVector = Icons.Filled.ExpandMore,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(id = R.string.expand_button_content_description)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ExpandLess,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(id = R.string.expand_button_content_description)
            )
        }
    }
}

@Composable
fun CardDescription(description: String, atk: Int?, def: Int?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            top = 8.dp,
            bottom = 16.dp,
            end = 16.dp
        )
    ) {
        if (atk != null && def != null) {
            Text (
                text = stringResource(id = R.string.card_parameters, atk, def),
                style = MaterialTheme.typography.h3
            )
        }

        Text (
            text = description,
            style = MaterialTheme.typography.body1
        )
    }
}