package com.musicgenres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.domain.musicgenres.GetMusicGenresUseCase
import com.ahmetocak.models.MusicGenreDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicGenreViewModel @Inject constructor(
    private val getMusicGenresUseCase: GetMusicGenresUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<MusicGenresUiState>(MusicGenresUiState.Loading)
    val uiState: StateFlow<MusicGenresUiState> = _uiState.asStateFlow()

    init {
        getMusicGenres()
    }

    private fun getMusicGenres() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = MusicGenresUiState.Loading
            when (val response = getMusicGenresUseCase()) {
                is Response.Success -> {
                    _uiState.value =
                        MusicGenresUiState.Success(musicGenresList = response.data.data)
                }

                is Response.Error -> {
                    _uiState.value = MusicGenresUiState.Error(message = response.errorMessage)
                }
            }
        }
    }
}

sealed interface MusicGenresUiState {
    object Loading : MusicGenresUiState
    data class Success(val musicGenresList: List<MusicGenreDetail>) : MusicGenresUiState
    data class Error(val message: UiText) : MusicGenresUiState
}