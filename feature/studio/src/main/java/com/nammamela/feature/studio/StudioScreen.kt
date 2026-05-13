package com.nammamela.feature.studio

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import com.nammamela.core.theme.SaffronAccent



data class StudioCastMember(
    val id: String,
    val name: String,
    val role: String,
    val giftsCount: Int = 0,
    val recentGiftType: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen() {
    val scrollState = rememberScrollState()

    // 📋 Troupe Roster Data State
    var castList by remember {
        mutableStateOf(
            listOf(
                StudioCastMember("1", "Raju Gowda", "Arjuna", 14, "👑"),
                StudioCastMember("2", "Shruthi", "Draupadi", 28, "🧣"),
                StudioCastMember("3", "Kempegowda", "Krishna", 9, "📿")
            )
        )
    }

    // ✍️ Text Fields States
    var actorName by remember { mutableStateOf("") }
    var actorRole by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }
    var isRoleError by remember { mutableStateOf(false) }

    // 📷 Poster Upload State
    var uploadProgress by remember { mutableStateOf<Float?>(null) }
    var isUploaded by remember { mutableStateOf(false) }

    // 🎖️ Patronage Tipping State
    var activePatronageArtist by remember { mutableStateOf<StudioCastMember?>(null) }
    var tippingProcessingState by remember { mutableStateOf<String?>(null) } // null, "processing", "success"
    var tippingSelectedAmount by remember { mutableStateOf(50) }
    var tippingSelectedEmoji by remember { mutableStateOf("👑") }
    var tippingSelectedName by remember { mutableStateOf("Mysore Peta") }

    LaunchedEffect(uploadProgress) {
        if (uploadProgress != null && uploadProgress!! < 1f) {
            delay(200)
            uploadProgress = uploadProgress!! + 0.2f
        } else if (uploadProgress != null && uploadProgress!! >= 1f) {
            delay(500)
            isUploaded = true
            uploadProgress = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "Studio",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Manage your troupe, schedule and artists",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Live Occupancy Donut Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Live Occupancy Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("Real-time counter across local booking boxes", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(20.dp))
                OccupancyChart(booked = 145, available = 55)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Management Header
        Text("Management", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp), color = MaterialTheme.colorScheme.onBackground)
        
        // 📷 Upload Poster Action (With interactive progress)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f)), RoundedCornerShape(24.dp))
                .clickable(enabled = uploadProgress == null) {
                    isUploaded = false
                    uploadProgress = 0f
                }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uploadProgress != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Uploading Digital Play Poster...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = uploadProgress!!,
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.LightGray.copy(alpha = 0.2f)
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (isUploaded) "✅" else "📸", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isUploaded) "Poster Uploaded Successfully!" else "Upload Play Poster",
                        color = if (isUploaded) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 🎖️ Troupe Roster List View
        Text("Troupe Roster", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp), color = MaterialTheme.colorScheme.onBackground)
        
        Card(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (castList.isEmpty()) {
                    Text(
                        text = "No cast members registered yet. Use the form below to register artists.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                } else {
                    castList.forEachIndexed { index, member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🎭", fontSize = 20.sp)
                                    // Gifting indicator badge
                                    if (member.recentGiftType != null) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surface)
                                                .border(0.5.dp, Color.LightGray, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(member.recentGiftType, fontSize = 10.sp)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = member.name,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Role: ${member.role}",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (member.giftsCount > 0) {
                                    Text(
                                        text = "🎖️ ${member.giftsCount}",
                                        fontSize = 13.sp,
                                        color = Color(0xFFD97706),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                }
                                
                                Button(
                                    onClick = { activePatronageArtist = member },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent.copy(alpha = 0.15f)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Tip UPI", color = SaffronAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                        if (index < castList.lastIndex) {
                            Divider(color = Color.LightGray.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ✍️ Add Cast Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Add Cast Member", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("Register actor name and role below", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = actorName,
                    onValueChange = {
                        actorName = it
                        isNameError = false
                    },
                    label = { Text("Actor Name") },
                    isError = isNameError,
                    supportingText = { if (isNameError) Text("Actor name cannot be empty", color = Color.Red) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = actorRole,
                    onValueChange = {
                        actorRole = it
                        isRoleError = false
                    },
                    label = { Text("Role in Play") },
                    isError = isRoleError,
                    supportingText = { if (isRoleError) Text("Role playing cannot be empty", color = Color.Red) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        var hasError = false
                        if (actorName.isBlank()) {
                            isNameError = true
                            hasError = true
                        }
                        if (actorRole.isBlank()) {
                            isRoleError = true
                            hasError = true
                        }
                        
                        if (!hasError) {
                            val newMember = StudioCastMember(
                                id = "S${System.currentTimeMillis()}",
                                name = actorName.trim(),
                                role = actorRole.trim()
                            )
                            castList = castList + newMember
                            actorName = ""
                            actorRole = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Profile", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    // ==========================================
    // 🎖️ Ranga-Siri UPI Gifting & Patronage Dialog
    // ==========================================
    activePatronageArtist?.let { artist ->
        if (tippingProcessingState == null) {
            AlertDialog(
                onDismissRequest = { activePatronageArtist = null },
                title = {
                    Text(
                        text = "🎖️ Ranga-Siri Patronage",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Bestow a traditional honor to ${artist.name} (${artist.role}). Proceeds go directly to sustaining folk arts.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val gifts = listOf(
                            Triple("👑", "Mysore Peta", 50),
                            Triple("🧣", "Zari Silk Shawl", 100),
                            Triple("📿", "Sandalwood Garland", 250)
                        )

                        gifts.forEach { (emoji, giftName, amount) ->
                            val isSelected = tippingSelectedAmount == amount
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) SaffronAccent else Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(if (isSelected) SaffronAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        tippingSelectedAmount = amount
                                        tippingSelectedEmoji = emoji
                                        tippingSelectedName = giftName
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(emoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(giftName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                }
                                Text("₹$amount", fontWeight = FontWeight.ExtraBold, color = SaffronAccent, fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            tippingProcessingState = "processing"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simulate UPI Payment (₹$tippingSelectedAmount)", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { activePatronageArtist = null },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text("Cancel", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        } else if (tippingProcessingState == "processing") {
            // Processing Screen Mock
            LaunchedEffect(Unit) {
                delay(1500)
                tippingProcessingState = "success"
            }
            
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Processing UPI Payment intent...", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Connecting with BHIM UPI safe server...", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        } else if (tippingProcessingState == "success") {
            // Success Screen Mock
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(
                        "✅ Honors Rendered!",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF10B981),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Presented $tippingSelectedEmoji $tippingSelectedName successfully to ${artist.name}!",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Funds of ₹$tippingSelectedAmount routed via instant UPI gateway settlement.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Update list state
                            castList = castList.map {
                                if (it.id == artist.id) {
                                    it.copy(
                                        giftsCount = it.giftsCount + 1,
                                        recentGiftType = tippingSelectedEmoji
                                    )
                                } else it
                            }
                            // Clean states
                            tippingProcessingState = null
                            activePatronageArtist = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Okay", fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun OccupancyChart(booked: Int, available: Int) {
    val total = booked + available
    val bookedAngle = (booked.toFloat() / total) * 360f

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = Color.LightGray.copy(alpha = 0.2f)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 40f, cap = StrokeCap.Round)
            )
            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = bookedAngle,
                useCenter = false,
                style = Stroke(width = 40f, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(booked.toFloat() / total * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurface, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("$booked / $total Booked", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
