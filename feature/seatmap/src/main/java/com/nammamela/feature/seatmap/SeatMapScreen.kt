package com.nammamela.feature.seatmap

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Composable
fun SeatMapScreen(
    showId: String,
    onConfirmBooking: (List<String>) -> Unit,
    viewModel: SeatMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedSeats by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(showId) {
        viewModel.initialize(showId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Stage Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(16.dp)
                .background(Color.DarkGray, RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("STAGE", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${uiState.error}", color = Color.Red)
            }
        } else {
            // AndroidView for RecyclerView with GridLayoutManager
            Box(modifier = Modifier.weight(1f).padding(16.dp)) {
                AndroidView(
                    factory = { ctx ->
                        val recyclerView = RecyclerView(ctx).apply {
                            layoutManager = GridLayoutManager(ctx, 6) // 6 seats per row
                            adapter = SeatAdapter(
                                seats = uiState.seats,
                                selectedSeats = selectedSeats,
                                onSeatClick = { seatId, isSelecting ->
                                    if (isSelecting) {
                                        selectedSeats = selectedSeats + seatId
                                    } else {
                                        selectedSeats = selectedSeats - seatId
                                    }
                                }
                            )
                        }
                        recyclerView
                    },
                    update = { view ->
                        (view.adapter as? SeatAdapter)?.updateData(uiState.seats, selectedSeats)
                    }
                )
            }
        }

        // Bottom Bar
        if (selectedSeats.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x1AFFFFFF)) // Glassmorphic hint
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${selectedSeats.size} Seats Selected", color = Color.White)
                        Text("₹${selectedSeats.size * 150}", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Button(
                        onClick = { onConfirmBooking(selectedSeats) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Hold to Confirm")
                    }
                }
            }
        }
    }
}

class SeatAdapter(
    private var seats: List<Seat>,
    private var selectedSeats: List<String>,
    private val onSeatClick: (String, Boolean) -> Unit
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    class SeatViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = View(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                100 // Fixed height
            ).apply { setMargins(8, 8, 8, 8) }
        }
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seats[position]
        val isSelected = selectedSeats.contains(seat.id)
        
        val color = when {
            isSelected -> android.graphics.Color.parseColor("#FF6B00") // Saffron for Selected
            seat.status == SeatStatus.AVAILABLE -> android.graphics.Color.DKGRAY
            seat.status == SeatStatus.BOOKED -> android.graphics.Color.parseColor("#1A1A2E") // StageNight indicating taken
            else -> android.graphics.Color.DKGRAY
        }
        
        holder.itemView.setBackgroundColor(color)
        holder.itemView.setOnClickListener {
            if (seat.status == SeatStatus.AVAILABLE) {
                onSeatClick(seat.id, !isSelected)
                notifyItemChanged(position) // For instantaneous feedback
            }
        }
    }

    override fun getItemCount() = seats.size

    fun updateData(newSeats: List<Seat>, newSelectedSeats: List<String>) {
        this.seats = newSeats
        this.selectedSeats = newSelectedSeats
        notifyDataSetChanged()
    }
}
