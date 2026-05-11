package com.nammamela.feature.fanwall

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Comment(val id: String, val user: String, val text: String, val isPinned: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FanWallScreen(showId: String) {
    var commentText by remember { mutableStateOf("") }
    val comments = listOf(
        Comment("1", "Manager (Troupe)", "Special guest appearance tonight by Dr. Rajkumar's family! Don't miss out.", isPinned = true),
        Comment("2", "Kiran G.", "The lighting in the second half is phenomenal!"),
        Comment("3", "Lakshmi S.", "My family comes to see this every year. True Company Nataka magic.")
    )

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        
        // Header
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text("Fan Wall", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
        }

        // Feed
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(comments) { comment ->
                CommentItem(comment)
            }
        }

        // Composer
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).navigationBarsPadding()
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Share your experience...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { /* Send */ },
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("↑", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    val bgColor = if (comment.isPinned) Color(0xFFFFE0B2) else Color.White
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(comment.user, fontWeight = FontWeight.Bold)
                        if (comment.isPinned) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PINNED", color = Color(0xFFE65100), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Text("2 hours ago", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(comment.text, style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ApplauseButton()
        }
    }
}

@Composable
fun ApplauseButton() {
    var isApplauded by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isApplauded) 1.4f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    Surface(
        onClick = { isApplauded = !isApplauded },
        shape = RoundedCornerShape(12.dp),
        color = if (isApplauded) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "👏",
                fontSize = 18.sp,
                modifier = Modifier.scale(scale)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isApplauded) "12" else "11",
                color = if (isApplauded) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
