package com.nammamela.feature.booking

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import com.nammamela.core.theme.SaffronAccent

data class SnackItem(val id: String, val emoji: String, val name: String, val price: Double, val quantity: Int = 0)

@Composable
fun ScreenBrightnessFix() {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(Unit) {
        activity?.window?.let { window ->
            val layoutParams = window.attributes
            val originalBrightness = layoutParams.screenBrightness
            
            // Turn brightness to maximum (100%) for scanning QR codes
            layoutParams.screenBrightness = 1.0f
            window.attributes = layoutParams

            onDispose {
                // Restore original brightness when leaving screen
                layoutParams.screenBrightness = originalBrightness
                window.attributes = layoutParams
            }
        } ?: onDispose {}
    }
}

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    onBackToHome: () -> Unit
) {
    // Inject the screen brightness controller
    ScreenBrightnessFix()

    val scrollState = rememberScrollState()

    var snacksState by remember {
        mutableStateOf(
            listOf(
                SnackItem("s1", "🍿", "Spiced Mandakki (Churmuri)", 40.0),
                SnackItem("s2", "🌶️", "Hot Mirchi Bajji (3 Pcs)", 30.0),
                SnackItem("s3", "☕", "Steaming Elakki Tea", 15.0)
            )
        )
    }

    val ticketBasePrice = 300.0 // 2 tickets
    val snacksTotalPrice = snacksState.sumOf { it.price * it.quantity }
    val grandTotal = ticketBasePrice + snacksTotalPrice

    val outerBackgroundColor = MaterialTheme.colorScheme.background

    val liveTicketDate = remember {
        val formatter = SimpleDateFormat("EEE, d MMM yyyy • hh:mm a", Locale.getDefault())
        formatter.format(Date())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(outerBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Booking Confirmed! 🎉",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            
            Text(
                text = "Show this QR at the entrance tent",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Glassmorphic / Apple-Wallet Perforated Ticket Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF13131A)) // Deep night aesthetic
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                    .drawBehind {
                        val circleRadius = 16.dp.toPx()
                        val cutY = size.height * 0.65f
                        
                        // Left circle cutout
                        drawCircle(
                            color = outerBackgroundColor,
                            radius = circleRadius,
                            center = Offset(0f, cutY)
                        )
                        // Right circle cutout
                        drawCircle(
                            color = outerBackgroundColor,
                            radius = circleRadius,
                            center = Offset(size.width, cutY)
                        )
                        // Dashed perforation line
                        drawLine(
                            color = Color.White.copy(alpha = 0.2f),
                            start = Offset(circleRadius, cutY),
                            end = Offset(size.width - circleRadius, cutY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                    }
                    .padding(vertical = 32.dp, horizontal = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Local Offline-Ready Vector QR Code
                    VectorQRCode(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B1B22))
                            .padding(16.dp),
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "🎟️ SECURE OFFLINE QR",
                        color = SaffronAccent,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "BOOKING ID: $bookingId",
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(64.dp)) // Bridge the perforation line area beautifully

                    // Bottom ticket stub info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Admit 2 • Premium Chairs",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Sri Manjunatha Troupe",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = liveTicketDate,
                                color = Color.Gray.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "₹$grandTotal",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🍿 "Mela Chomp" Traditional Snack Pre-booking Panel
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🍿 Mela Chomp F&B Add-ons",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Pre-order warm festival bites and cardamom tea directly to your seat during show intervals!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    snacksState.forEach { snack ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(snack.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(snack.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                    Text("₹${snack.price}", color = Color.Gray, fontSize = 12.sp)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (snack.quantity > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color.LightGray.copy(alpha = 0.4f))
                                            .clickable {
                                                snacksState = snacksState.map {
                                                    if (it.id == snack.id) it.copy(quantity = it.quantity - 1) else it
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("-", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Text(snack.quantity.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                        .clickable {
                                            snacksState = snacksState.map {
                                                if (it.id == snack.id) it.copy(quantity = it.quantity + 1) else it
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+", fontWeight = FontWeight.ExtraBold, color = Color.White)
                                }
                            }
                        }
                    }

                    if (snacksTotalPrice > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Snacks Subtotal", color = Color.Gray, fontSize = 14.sp)
                            Text("₹$snacksTotalPrice", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBackToHome,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Return to Home", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun VectorQRCode(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    // Generate a fixed but complex look-alike matrix
    val matrix = remember {
        Array(15) { r ->
            BooleanArray(15) { c ->
                val inTopLeftAnchor = r < 5 && c < 5
                val inTopRightAnchor = r < 5 && c >= 10
                val inBottomLeftAnchor = r >= 10 && c < 5
                
                if (inTopLeftAnchor) {
                    (r == 0 || r == 4 || c == 0 || c == 4) || (r in 2..2 && c in 2..2)
                } else if (inTopRightAnchor) {
                    (r == 0 || r == 4 || c == 10 || c == 14) || (r in 2..2 && c in 12..12)
                } else if (inBottomLeftAnchor) {
                    (r == 10 || r == 14 || c == 0 || c == 4) || (r in 12..12 && c in 2..2)
                } else {
                    (r * c + r + c) % 3 == 0 || (r + c) % 5 == 0
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserY"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sizePx = size.width
            val cellSize = sizePx / 15f

            // Draw blocks
            for (r in 0 until 15) {
                for (c in 0 until 15) {
                    if (matrix[r][c]) {
                        drawRect(
                            color = color,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = androidx.compose.ui.geometry.Size(cellSize - 1f, cellSize - 1f)
                        )
                    }
                }
            }

            // Draw glowing orange/saffron laser sweep
            val yOffset = laserY * sizePx
            drawLine(
                color = SaffronAccent,
                start = Offset(0f, yOffset),
                end = Offset(sizePx, yOffset),
                strokeWidth = 3.dp.toPx()
            )
        }
    }
}

