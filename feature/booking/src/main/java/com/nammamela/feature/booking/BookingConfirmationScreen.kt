package com.nammamela.feature.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    onBackToHome: () -> Unit
) {
    // Simulate Room DB Write on LaunchedEffect
    LaunchedEffect(Unit) {
        // Mock Room DB Write: bookingDao.insert(BookingEntity(...))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Booking Confirmed!",
                color = MaterialTheme.colorScheme.tertiary, // Festival Teal
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Show this QR at the entrance",
                color = Color.LightGray,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Glassmorphic Ticket Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // QR Code Placeholder
                    AsyncImage(
                        model = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=$bookingId",
                        contentDescription = "QR Code",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(200.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "ID: $bookingId",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Admit 2 • VIP Seats",
                        color = MaterialTheme.colorScheme.secondary, // Marigold
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onBackToHome() },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Return to Home", fontSize = 16.sp)
            }
        }
    }
}
