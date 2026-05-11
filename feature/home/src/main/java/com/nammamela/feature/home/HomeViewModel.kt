package com.nammamela.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Show(val id: String, val title: String, val troupe: String, val imageUrl: String)

data class HomeUiState(
    val isLoading: Boolean = true,
    val shows: List<Show> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                delay(800)
                
                val shows = listOf(
                    Show("1", "Kurukshetra", "Sri Manjunatha Troupe", "file:///android_asset/images/kurukshetra.png"),
                    Show("2", "Rakta Ratri", "Veerabhadreshwara Krupa", "file:///android_asset/images/rakta_ratri.png"),
                    Show("3", "Dakshayagna", "Gubbi Veeranna Nataka Company", "file:///android_asset/images/kurukshetra.png")
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    shows = shows
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "An unexpected error occurred"
                )
            }
        }
    }
}
