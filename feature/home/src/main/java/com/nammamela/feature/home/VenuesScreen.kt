package com.nammamela.feature.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.core.theme.SaffronAccent
import com.nammamela.core.theme.TealAccent
import com.nammamela.core.theme.AppState
import com.nammamela.core.theme.AppLanguage
import com.nammamela.core.theme.translate

data class NomadicTent(
    val id: String,
    val name: String,
    val troupe: String,
    val village: String,
    val dates: String,
    val status: String, // "ACTIVE", "NEXT_WEEK", "UPCOMING"
    val latitude: Double,
    val longitude: Double,
    val capacity: String,
    val ticketPrice: String,
    val mapX: Float, // Relative X coordinate (0.0 to 1.0) on Karnataka Map
    val mapY: Float  // Relative Y coordinate (0.0 to 1.0) on Karnataka Map
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuesScreen() {
    val context = LocalContext.current
    val tents = remember {
        listOf(
            NomadicTent(
                id = "T1",
                name = "Sri Siddharoodha Tent",
                troupe = "Sri Manjunatha Troupe",
                village = "Channapatna (Field B)",
                dates = "May 10 - May 15",
                status = "ACTIVE",
                latitude = 12.6518,
                longitude = 77.2014,
                capacity = "350 seats",
                ticketPrice = "₹120",
                mapX = 0.53f,
                mapY = 0.74f
            ),
            NomadicTent(
                id = "T2",
                name = "Shakambhari Nataka Mandali",
                troupe = "Veerabhadreshwara Krupa",
                village = "Ramanagara (Silk Field)",
                dates = "May 18 - May 24",
                status = "NEXT_WEEK",
                latitude = 12.7214,
                longitude = 77.2847,
                capacity = "400 seats",
                ticketPrice = "₹100",
                mapX = 0.58f,
                mapY = 0.72f
            ),
            NomadicTent(
                id = "T3",
                name = "Kranthi Raitha Sangha",
                troupe = "Gubbi Veeranna Nataka Company",
                village = "Mandya (Sugarcane Ground)",
                dates = "May 28 - Jun 04",
                status = "UPCOMING",
                latitude = 12.5218,
                longitude = 76.8951,
                capacity = "300 seats",
                ticketPrice = "₹150",
                mapX = 0.46f,
                mapY = 0.76f
            ),
            NomadicTent(
                id = "T4",
                name = "Sri Kalika Prasadita Mandali",
                troupe = "Saligrama Yakshagana Mandali",
                village = "Mangaluru (Kadri Ground)",
                dates = "Jun 08 - Jun 15",
                status = "UPCOMING",
                latitude = 12.8701,
                longitude = 74.8801,
                capacity = "500 seats",
                ticketPrice = "₹200",
                mapX = 0.22f,
                mapY = 0.72f
            ),
            NomadicTent(
                id = "T5",
                name = "Kittur Chennamma Bayalata Troupe",
                troupe = "Kittur Chennamma Bayalata Troupe",
                village = "Belagavi (Kittur Fort)",
                dates = "Jun 20 - Jun 28",
                status = "UPCOMING",
                latitude = 15.8497,
                longitude = 74.4977,
                capacity = "600 seats",
                ticketPrice = "₹150",
                mapX = 0.13f,
                mapY = 0.31f
            )
        )
    }

    var selectedTent by remember { mutableStateOf(tents.first()) }

    // Pulse animation for active pins
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "📍 " + "Nomadic Tents".translate(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Live touring locations of folk theaters and bayalata tents".translate(),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 🗺️ Vector Karnataka Geography Map Box (Enlarged for amazing clarity!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(if (isSystemInDarkTheme()) Color(0xFF101018) else Color(0xFFE5F1FF))
                .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f)), RoundedCornerShape(24.dp))
        ) {
            val isDark = isSystemInDarkTheme()
            val gridColor = if (isDark) Color(0x06FFFFFF) else Color(0x08000000)
            val borderStrokeColor = if (isDark) SaffronAccent.copy(alpha = 0.35f) else SaffronAccent.copy(alpha = 0.5f)
            val fillSolidColor = if (isDark) Color(0x12FFFFFF) else Color(0x28FFFFFF)

            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // 1. Draw Topographic Coordinate Grids
                for (i in 1..10) {
                    val x = w * (i / 11f)
                    drawLine(gridColor, Offset(x, 0f), Offset(x, h), strokeWidth = 1f)
                }
                for (i in 1..8) {
                    val y = h * (i / 9f)
                    drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1f)
                }

                // 2. High-Fidelity Highly Realistic 18-Point Vector Outline of Karnataka
                val karnatakaPath = Path().apply {
                    moveTo(w * 0.58f, h * 0.06f) // Bidar (Extreme North)
                    lineTo(w * 0.64f, h * 0.12f) // Aurad Border
                    lineTo(w * 0.68f, h * 0.20f) // Kalaburagi
                    lineTo(w * 0.76f, h * 0.28f) // Yadgir
                    lineTo(w * 0.70f, h * 0.38f) // Raichur Bulge
                    lineTo(w * 0.64f, h * 0.44f) // Ballari Corner
                    lineTo(w * 0.68f, h * 0.58f) // Chitradurga / Pavagada Bulge
                    lineTo(w * 0.82f, h * 0.68f) // Kolar (Extreme East)
                    lineTo(w * 0.70f, h * 0.74f) // Chikkaballapura
                    lineTo(w * 0.54f, h * 0.94f) // Chamarajanagar (Extreme South Tip)
                    lineTo(w * 0.42f, h * 0.86f) // Mysuru / H.D. Kote
                    lineTo(w * 0.32f, h * 0.80f) // Kodagu
                    lineTo(w * 0.22f, h * 0.74f) // Mangaluru Coastal Border
                    lineTo(w * 0.18f, h * 0.60f) // Udupi Shore
                    lineTo(w * 0.12f, h * 0.44f) // Karwar Coast
                    lineTo(w * 0.06f, h * 0.30f) // Belagavi (Extreme West)
                    lineTo(w * 0.20f, h * 0.24f) // Vijayapura Border
                    lineTo(w * 0.42f, h * 0.14f) // Bagalkot
                    close()
                }

                // Fill Karnataka vector base
                drawPath(path = karnatakaPath, color = fillSolidColor)

                // 2.5 Regional mela popularity rate overlays clipped inside borders
                clipPath(karnatakaPath) {
                    drawCircle(color = Color(0x6010B981), center = Offset(w * 0.18f, h * 0.65f), radius = w * 0.18f) // Coastal Green (Above 80%)
                    drawCircle(color = Color(0x6084CC16), center = Offset(w * 0.32f, h * 0.62f), radius = w * 0.18f) // Malnad Lime (75-80%)
                    drawCircle(color = Color(0x60F97316), center = Offset(w * 0.58f, h * 0.78f), radius = w * 0.22f) // South Orange (70-75%)
                    drawCircle(color = Color(0x60EF4444), center = Offset(w * 0.68f, h * 0.22f), radius = w * 0.16f) // Kalaburagi Red (60-70%)
                    drawCircle(color = Color(0x60F97316), center = Offset(w * 0.58f, h * 0.08f), radius = w * 0.10f) // Bidar Orange (70-75%)
                    drawCircle(color = Color(0x60F97316), center = Offset(w * 0.10f, h * 0.32f), radius = w * 0.14f) // Belagavi Orange (70-75%)
                    drawCircle(color = Color(0x751F2937), center = Offset(w * 0.70f, h * 0.32f), radius = w * 0.14f) // Raichur Black (below 60%)
                }

                // Draw State border stroke
                drawPath(path = karnatakaPath, color = borderStrokeColor, style = Stroke(width = 4f))

                // 3. Draw Major district centers as anchor reference nodes
                val hubs = listOf(
                    Pair("Belagavi", Offset(w * 0.10f, h * 0.32f)),
                    Pair("Kalaburagi", Offset(w * 0.68f, h * 0.22f)),
                    Pair("Mangaluru", Offset(w * 0.22f, h * 0.74f)),
                    Pair("Bengaluru", Offset(w * 0.64f, h * 0.72f))
                )
                hubs.forEach { hub ->
                    // Hub circle anchor
                    drawCircle(color = SaffronAccent.copy(alpha = 0.5f), radius = 5f, center = hub.second)
                }
            }

            // 5. Beautiful Compact Floating Glassmorphic Legend Guidelines (Avoids covering any data!)
            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .width(115.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Mela Popularity Rate Of Karnataka".translate().uppercase(),
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 9.sp
                    )
                    
                    Divider(color = Color.LightGray.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 1.dp))

                    LegendRow(color = Color(0xFF10B981), text = "Above 80%".translate())
                    LegendRow(color = Color(0xFF84CC16), text = "75-80%".translate())
                    LegendRow(color = Color(0xFFF97316), text = "70-75%".translate())
                    LegendRow(color = Color(0xFFEF4444), text = "60-70%".translate())
                    LegendRow(color = Color(0xFF1F2937), text = "below 60%".translate())
                }
            }

            // 4. Overlaid interactive Tent pins placed exactly over vector map points
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val wPx = maxWidth
                val hPx = maxHeight

                tents.forEach { tent ->
                    val isSelected = selectedTent.id == tent.id
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(
                                x = (wPx * tent.mapX) - 25.dp, // Center perfectly by subtracting radius
                                y = (hPx * tent.mapY) - 25.dp
                            )
                            .clickable { selectedTent = tent }
                    ) {
                        // Pulsing ripple ring around active pin
                        if (tent.status == "ACTIVE") {
                            Canvas(modifier = Modifier.size(50.dp)) {
                                drawCircle(
                                    color = SaffronAccent.copy(alpha = pulseAlpha),
                                    radius = pulseRadius,
                                    center = Offset(25.dp.toPx(), 25.dp.toPx()),
                                    style = Stroke(width = 3f)
                                )
                            }
                        }

                        // Core Pin bubble
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) SaffronAccent else MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                                .border(
                                    2.dp,
                                    if (isSelected) Color.White else SaffronAccent,
                                    CircleShape
                                )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "🎪",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Selected Tent Detail Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (selectedTent.status) {
                            "ACTIVE" -> Color(0xFF10B981).copy(alpha = 0.15f)
                            "NEXT_WEEK" -> SaffronAccent.copy(alpha = 0.15f)
                            else -> Color.Gray.copy(alpha = 0.15f)
                        }
                    ) {
                        Text(
                            text = when (selectedTent.status) {
                                "ACTIVE" -> "● TOURING NOW".translate()
                                "NEXT_WEEK" -> "⏱️ NEXT WEEK".translate()
                                else -> "📅 UPCOMING".translate()
                            },
                            color = when (selectedTent.status) {
                                "ACTIVE" -> Color(0xFF10B981)
                                "NEXT_WEEK" -> SaffronAccent
                                else -> Color.Gray
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = selectedTent.ticketPrice,
                        fontWeight = FontWeight.ExtraBold,
                        color = SaffronAccent,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = selectedTent.name.translate(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedTent.troupe.translate(),
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Color.LightGray.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("FIELD / VILLAGE".translate(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(selectedTent.village, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("DATES".translate(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(selectedTent.dates, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:${selectedTent.latitude},${selectedTent.longitude}?q=${selectedTent.latitude},${selectedTent.longitude}(${selectedTent.name})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text(
                        "Route to Tent Field (GPS)".translate(),
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Selector Horizontal List
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tents) { tent ->
                val isSelected = selectedTent.id == tent.id
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) SaffronAccent.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) SaffronAccent else Color.LightGray.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.clickable { selectedTent = tent }
                ) {
                    Text(
                        text = tent.name.translate().take(18) + "...",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        color = if (isSelected) SaffronAccent else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LegendRow(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
