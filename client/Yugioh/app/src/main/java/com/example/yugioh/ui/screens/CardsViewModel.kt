package com.example.yugioh.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yugioh.CardsApplication
import com.example.yugioh.data.CardsRepository
import com.example.yugioh.model.CardModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CardsViewModel(private val cardsRepository: CardsRepository): ViewModel() {
    var cardsUiState: CardsUiState by mutableStateOf(CardsUiState.Loading)
        private set

    var items: List<CardModel> by mutableStateOf(listOf())
        private set
    var totalItems: Int by mutableStateOf(0)
        private set
    var page: Int by mutableStateOf(1)
        private set
    var search: String by mutableStateOf("")
        private set

    private val _textSearch = MutableStateFlow("")
    val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        _getCards(1, null, 15)

        viewModelScope.launch {
            textSearch.debounce(1000).collect {
                items = emptyList()
                totalItems = 0
                page = 1
                search = it.trim()
                _getCards(page, search)
            }
        }
    }

    fun _getCards(page: Int, search: String? = null, itemsPerPage: Int? = null) {
        viewModelScope.launch {
            cardsUiState = CardsUiState.Loading

            val listResult = cardsRepository.getCards(page, search, itemsPerPage)
            Log.d("cardsvm","getting items page ${page} items ${totalItems}")
            totalItems = listResult.totalItems
            if (listResult.items.isNotEmpty()) {
                items += listResult.items
            }
//            cardsUiState = try {
//                val listResult = cardsRepository.getCards(page, search, itemsPerPage)
//                Log.d("cardsvm","getting items page ${page} items ${totalItems}")
//                totalItems = listResult.totalItems
//                if (listResult.items.isNotEmpty()) {
//                    items += listResult.items
//                }
//                CardsUiState.Success(listResult.items)
//            } catch (e: IOException) {
//               CardsUiState.Error
//            } catch (e: HttpException) {
//                CardsUiState.Error
//            }
        }
    }

    fun _postCard(
        context: Context,
        name: String,
        type: String,
        description: String,
        atk: Int?,
        def: Int?
    ) {
        viewModelScope.launch {
            val cardModel = CardModel(
                name = name,
                type = type,
                description = description,
                atk = atk ?: null,
                def = def ?: null
            )

            val call: Call<CardModel> = cardsRepository.postCard(cardModel)

            call!!.enqueue(object: Callback<CardModel> {
                override fun onResponse(call: Call<CardModel>, response: Response<CardModel>) {
                    Toast.makeText(context, "${name} posted to card list!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<CardModel>, t: Throwable) {
                    Toast.makeText(context, "${name} creation failed!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    val postCard: (
        context: Context,
        name: String,
        type: String,
        description: String,
        atk: Int?,
        def: Int?
    ) -> Unit = {
            context: Context,
            name: String,
            type: String,
            description: String,
            atk: Int?,
            def: Int? ->
        _postCard(
            context = context,
            name = name,
            type = type,
            description = description,
            atk = atk ?: null,
            def = def ?: null
        )
    }

    val getMoreCards: () -> Unit = {
        Log.d("cardsvm", "size: ${this.items.size}")
        if (this.items.isEmpty() || this.items.size < this.totalItems) {
            this.page++
            _getCards(this.page, this.search)
        }
    }

    val setSearchText: (it: String) -> Unit = {
        _textSearch.value = it
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CardsApplication)
                val cardsRepository = application.container.cardsRepository
                CardsViewModel(cardsRepository = cardsRepository)
            }
        }
    }
}