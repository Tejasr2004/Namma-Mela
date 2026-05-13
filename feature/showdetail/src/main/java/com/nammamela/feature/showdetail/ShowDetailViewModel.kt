package com.nammamela.feature.showdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class CastMember(val id: String, val name: String, val role: String, val imageUrl: String)

data class Showtime(
    val id: String,
    val label: String,
    val time: String,
    val activeVillage: String,
    val latitude: Double,
    val longitude: Double
)

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
    val showtimes: List<Showtime> = emptyList(),
    val selectedShowtime: Showtime? = null,
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
                
                // Dynamically build metadata and custom casts for all 5 unique productions!
                val showTitle = when (showId) {
                    "1" -> "Kurukshetra"
                    "2" -> "Rakta Ratri"
                    "3" -> "Dakshayagna"
                    "4" -> "Shanishwara Mahatme"
                    "5" -> "Prahlada Charitre"
                    else -> "Kurukshetra"
                }

                val showTroupe = when (showId) {
                    "1" -> "Sri Manjunatha Troupe"
                    "2" -> "Veerabhadreshwara Krupa"
                    "3" -> "Gubbi Veeranna Nataka Company"
                    "4" -> "Sri Durga Devi Prasanna Sabha"
                    "5" -> "Saligrama Yakshagana Mandali"
                    else -> "Sri Manjunatha Troupe"
                }

                val showGenre = when (showId) {
                    "1" -> "Mythology / Epic Drama"
                    "2" -> "Historical Action Play"
                    "3" -> "Mythological Saga"
                    "4" -> "Devotional / Cosmic"
                    "5" -> "Devotional / Mythological"
                    else -> "Mythology / Drama"
                }

                val showDuration = when (showId) {
                    "1" -> "3h 15m"
                    "2" -> "2h 50m"
                    "3" -> "3h 05m"
                    "4" -> "2h 45m"
                    "5" -> "3h 00m"
                    else -> "3 Hours"
                }

                val showPoster = when (showId) {
                    "1" -> "file:///android_asset/images/kurukshetra_poster.png"
                    "2" -> "file:///android_asset/images/rakta_ratri_poster.png"
                    "3" -> "file:///android_asset/images/dakshayagna_poster.png"
                    "4" -> "file:///android_asset/images/shanishwara_poster.png"
                    "5" -> "file:///android_asset/images/prahlada_poster.png"
                    else -> "file:///android_asset/images/kurukshetra_poster.png"
                }

                val showSynopsis = when (showId) {
                    "1" -> "A grand retelling of the Mahabharata focusing on the epic battle of Kurukshetra. Featuring state-of-the-art stage mechanics, thunderous live drums, and soul-stirring music typical of Company Nataka style."
                    "2" -> "A gripping narrative of espionage, royalty, and battle set in medieval southern India. Relive the courage of historical warriors through highly intense sword fights and classic folk melodies."
                    "3" -> "The classical cosmic conflict between Shiva and King Daksha. Experience the magnificent, raging dance of Shiva (Rudra Tandava) and the emotional sacrifice that shook the heavens."
                    "4" -> "The celestial trial and spiritual journey of King Vikrama as he faces the cosmic gaze of Lord Shanishwara. A profound, emotional, and soul-cleansing musical performance with heavy metal cymbals."
                    "5" -> "The legendary story of the young child Prahlada's unwavering devotion to Lord Vishnu, culminating in the fierce, thunderous appearance of Narasimha Avatar through real wooden pillars!"
                    else -> ""
                }

                val castList = when (showId) {
                    "1" -> listOf(
                        CastMember("c11", "Raju Gowda", "Arjuna", "https://upload.wikimedia.org/wikipedia/commons/9/90/Yakshagana_Headdress.jpg"),
                        CastMember("c12", "Shruthi", "Draupadi", "https://upload.wikimedia.org/wikipedia/commons/0/07/Yakshagana_performance_at_Banasankari.jpg"),
                        CastMember("c13", "Kempegowda", "Krishna", "https://upload.wikimedia.org/wikipedia/commons/9/91/Krishna_Yakshagana_Artist.jpg")
                    )
                    "2" -> listOf(
                        CastMember("c21", "Vikram Hegde", "Veerabhadra", "https://upload.wikimedia.org/wikipedia/commons/e/e4/Yakshagana_Demon_Character.jpg"),
                        CastMember("c22", "Niveditha", "Queen Chennamma", "https://upload.wikimedia.org/wikipedia/commons/1/1d/Yakshagana_Stree_Vesha.jpg"),
                        CastMember("c23", "Somashekhar", "Commander Virupa", "https://upload.wikimedia.org/wikipedia/commons/8/86/Yakshagana_Heroic_Character.jpg")
                    )
                    "3" -> listOf(
                        CastMember("c31", "Chandrashekhar", "Lord Shiva", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Yakshagana_Shiva_Character.jpg"),
                        CastMember("c32", "Ananth Murthy", "King Daksha", "https://upload.wikimedia.org/wikipedia/commons/5/53/Yakshagana_Daksha.jpg"),
                        CastMember("c33", "Saraswathi", "Sati", "https://upload.wikimedia.org/wikipedia/commons/b/ba/Yakshagana_Female_Role.jpg")
                    )
                    "4" -> listOf(
                        CastMember("c41", "Ganesh Bhat", "Shanishwara", "https://upload.wikimedia.org/wikipedia/commons/c/cd/Yakshagana_Shani.jpg"),
                        CastMember("c42", "Prasad Rao", "King Vikrama", "https://upload.wikimedia.org/wikipedia/commons/e/ef/Yakshagana_King_Role.jpg"),
                        CastMember("c43", "Vasudeva", "Narada Muni", "https://upload.wikimedia.org/wikipedia/commons/8/8f/Yakshagana_Narada.jpg")
                    )
                    "5" -> listOf(
                        CastMember("c51", "Master Chetan", "Prahlada", "https://upload.wikimedia.org/wikipedia/commons/d/dd/Yakshagana_Child_Artist.jpg"),
                        CastMember("c52", "Manjunath Acharya", "Hiranya", "https://upload.wikimedia.org/wikipedia/commons/0/09/Yakshagana_Demon_Hiranya.jpg"),
                        CastMember("c53", "Subramanya", "Narasimha", "https://upload.wikimedia.org/wikipedia/commons/a/a9/Yakshagana_Narasimha.jpg")
                    )
                    else -> emptyList()
                }

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

                val todayStr = "TODAY • " + dateFormat.format(calendar.time).uppercase()

                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val tomorrowStr = "TOMORROW • " + dateFormat.format(calendar.time).uppercase()

                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val dayAfterStr = dateFormat.format(calendar.time).uppercase()

                val showtimesList = listOf(
                    Showtime(
                        id = "st1",
                        label = todayStr,
                        time = "06:30 PM",
                        activeVillage = "Sirsi (Kalyana Mantapa Grounds)",
                        latitude = 14.6195,
                        longitude = 74.8441
                    ),
                    Showtime(
                        id = "st2",
                        label = tomorrowStr,
                        time = "10:00 PM",
                        activeVillage = "Gadag (Mela Stadium Grounds)",
                        latitude = 15.4225,
                        longitude = 75.6263
                    ),
                    Showtime(
                        id = "st3",
                        label = dayAfterStr,
                        time = "02:30 PM",
                        activeVillage = "Haveri (Zilla Panchayat Grounds)",
                        latitude = 14.7954,
                        longitude = 75.3992
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    title = showTitle,
                    troupe = showTroupe,
                    duration = showDuration,
                    genre = showGenre,
                    synopsis = showSynopsis,
                    posterUrl = showPoster,
                    cast = castList,
                    showtimes = showtimesList,
                    selectedShowtime = showtimesList.firstOrNull()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Failed to load show details"
                )
            }
        }
    }

    fun selectShowtime(showtime: Showtime) {
        _uiState.value = _uiState.value.copy(selectedShowtime = showtime)
    }
}

