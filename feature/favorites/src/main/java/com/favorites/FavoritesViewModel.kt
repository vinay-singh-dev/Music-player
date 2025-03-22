package com.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.domain.favorites.DeleteFavoriteSongUseCase
import com.ahmetocak.domain.GetAllFavoriteSongsUseCase
import com.ahmetocak.models.FavoriteSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getAllFavoriteSongsUseCase: GetAllFavoriteSongsUseCase,
    private val deleteFavoriteSongUseCase: DeleteFavoriteSongUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        getAllFavoriteSongs()
    }

    fun getAllFavoriteSongs() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getAllFavoriteSongsUseCase()) {
                is Response.Success -> {
                    response.data.collect { favoriteSongs ->
                        _uiState.update {
                            it.copy(favoriteSongsList = favoriteSongs)
                        }
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(userMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    fun removeFavoriteSong(songId: Long) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = deleteFavoriteSongUseCase(songId)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(userMessages = listOf(UiText.StringResource(R.string.fav_song_removed_message)))
                    }
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(userMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    fun consumedUserMessages() {
        _uiState.update {
            it.copy(userMessages = listOf())
        }
    }
}

data class FavoritesUiState(
    val userMessages: List<UiText> = listOf(),
    val favoriteSongsList: List<FavoriteSongs> = listOf()
)