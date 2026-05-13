package com.nammamela.feature.profile

import com.nammamela.core.theme.translate

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nammamela.core.theme.AppState
import com.nammamela.core.theme.SaffronAccent

data class BookingHistoryItem(val id: String, val showName: String, val date: String, val seats: String)

@Composable
fun ProfileScreen() {
    // Show secure AuthScreen if not logged in
    if (!AppState.isLoggedIn) {
        AuthScreen(onAuthSuccess = {})
        return
    }

    val history = listOf(
        BookingHistoryItem("B1029", "Kurukshetra (Bengaulru field)", "24 Oct 2026 • 08:30 PM", "Chair S12, S13"),
        BookingHistoryItem("B0912", "Rakta Ratri (Mysuru field)", "15 Sep 2026 • 09:00 PM", "Premium S4")
    )

    var activeTicket by remember { mutableStateOf<BookingHistoryItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header / Profile Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Text(
                text = "Profile".translate(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = AppState.loggedInUser?.split(" ")?.mapNotNull { it.firstOrNull() }?.joinToString("") ?: "U"
                    Text(
                        text = initials.take(2).uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = AppState.loggedInUser ?: "User Account",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = AppState.loggedInPhone ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Settings Card with Light/Dark Theme Switch & Logout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Preferences".translate(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Dark Mode Switch Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🌓 Dark Mode Theme".translate(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Switch between Dark and Light skins".translate(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = AppState.isDarkMode,
                        onCheckedChange = { AppState.isDarkMode = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = SaffronAccent, checkedTrackColor = SaffronAccent.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                // Logout CTA Button
                Button(
                    onClick = {
                        AppState.isLoggedIn = false
                        AppState.loggedInUser = null
                        AppState.loggedInPhone = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(
                        text = "🔒 Log Out Securely".translate(),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "My Tickets & History".translate(), 
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleLarge, 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(history) { item ->
                HistoryCard(item) {
                    activeTicket = item
                }
            }
        }
    }

    // ==========================================
    // 🎟️ Apple-Wallet Digital Ticket Stub Overlay
    // ==========================================
    activeTicket?.let { ticket ->
        Dialog(
            onDismissRequest = { activeTicket = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { activeTicket = null },
                contentAlignment = Alignment.Center
            ) {
                // Glassmorphic high-contrast admission card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF13131A)) // Deep night base
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                        .drawBehind {
                            val circleRadius = 14.dp.toPx()
                            val cutY = size.height * 0.65f
                            
                            // Perforation cuts on the edges
                            drawCircle(
                                color = Color.Transparent,
                                radius = circleRadius,
                                center = Offset(0f, cutY)
                            )
                            drawCircle(
                                color = Color.Transparent,
                                radius = circleRadius,
                                center = Offset(size.width, cutY)
                            )
                            // Dashed separator line
                            drawLine(
                                color = Color.White.copy(alpha = 0.2f),
                                start = Offset(circleRadius, cutY),
                                end = Offset(size.width - circleRadius, cutY),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            )
                        }
                        .clickable(enabled = false) {} // block dialog cancel tape click
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎟️ DIGITAL ADMISSION PASS".translate(),
                            color = SaffronAccent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Render our Vector QR Code locally
                        ProfileVectorQRCode(
                            modifier = Modifier
                                .size(170.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1B1B22))
                                .padding(16.dp),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${"BOOKING ID".translate()}: ${ticket.id}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(56.dp)) // Bridge perforation elegantly

                        // Bottom Ticket Details
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = ticket.showName,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("DATE & TIME".translate(), fontSize = 9.sp, color = Color.Gray)
                                    Text(ticket.date.split(" • ")[0], fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("SEATS REG".translate(), fontSize = 9.sp, color = Color.Gray)
                                    Text(ticket.seats, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { activeTicket = null },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Dismiss ticket".translate(), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: BookingHistoryItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.showName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("#${item.id}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text("DATE".translate(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(item.date.split(" • ")[0], style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("SEATS".translate(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(item.seats, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
fun ProfileVectorQRCode(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
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
