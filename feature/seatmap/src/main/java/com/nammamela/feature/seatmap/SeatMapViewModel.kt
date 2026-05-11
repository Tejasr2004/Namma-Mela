package com.nammamela.feature.seatmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SeatStatus { AVAILABLE, BOOKED, SELECTED }
data class Seat(val id: String, val status: SeatStatus)


data class SeatMapUiState(
    val isLoading: Boolean = true,
    val seats: List<Seat> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SeatMapViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SeatMapUiState())
    val uiState: StateFlow<SeatMapUiState> = _uiState.asStateFlow()

    private var currentShowId: String? = null

    fun initialize(showId: String) {
        if (currentShowId == showId) return
        currentShowId = showId
        loadMockSeats()
    }

    private fun loadMockSeats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            delay(500) // Mock network delay
            
            val newSeats = mutableListOf<Seat>()
            for (i in 1..60) {
                newSeats.add(Seat("S$i", if (i % 5 == 0) SeatStatus.BOOKED else SeatStatus.AVAILABLE))
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                seats = newSeats
            )
        }
    }
}
