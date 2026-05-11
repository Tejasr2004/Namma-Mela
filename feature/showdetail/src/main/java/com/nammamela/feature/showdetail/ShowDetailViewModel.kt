package com.nammamela.feature.showdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CastMember(val id: String, val name: String, val role: String, val imageUrl: String)

data class ShowDetailUiState(
    val isLoading: Boolean = true,
    val showId: String = "",
    val title: String = "",
    val troupe: String = "",
    val duration: String = "",
    val genre: String = "",
    val synopsis: String = "",
    val posterUrl: String = "",
    val cast: List<CastMember> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ShowDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ShowDetailUiState())
    val uiState: StateFlow<ShowDetailUiState> = _uiState.asStateFlow()

    fun fetchShowDetails(showId: String) {
        if (_uiState.value.showId == showId && !_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, showId = showId)
            try {
                delay(600)
                
                val castList = listOf(
                    CastMember("1", "Raju Gowda", "Arjuna", "file:///android_asset/images/lead_actor.png"),
                    CastMember("2", "Shruthi", "Draupadi", "file:///android_asset/images/lead_actor.png"),
                    CastMember("3", "Kempegowda", "Krishna", "file:///android_asset/images/lead_actor.png")
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    title = if (showId == "1") "Kurukshetra" else "Rakta Ratri",
                    troupe = if (showId == "1") "Sri Manjunatha Troupe" else "Veerabhadreshwara Krupa",
                    duration = "3 Hours",
                    genre = "Mythology / Drama",
                    synopsis = "A grand retelling of the Mahabharata focusing on the epic battle of Kurukshetra. Featuring state-of-the-art stage mechanics and soul-stirring music typical of Company Nataka style.",
                    posterUrl = if (showId == "1") "file:///android_asset/images/kurukshetra.png" else "file:///android_asset/images/rakta_ratri.png",
                    cast = castList
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Failed to load show details"
                )
            }
        }
    }
}
