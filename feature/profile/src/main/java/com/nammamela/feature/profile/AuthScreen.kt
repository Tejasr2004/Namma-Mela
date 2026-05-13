package com.nammamela.feature.profile

import com.nammamela.core.theme.translate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.core.theme.AppState
import com.nammamela.core.theme.SaffronAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var isSignUp by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var authErrorMessage by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "🔐 NAMMA MELA SECURE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronAccent,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isSignUp) "Create Account".translate() else "Welcome Back".translate(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = if (isSignUp) "Sign up to book nomadic mela seats safely".translate() else "Sign in securely to manage bookings and troupe".translate(),
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login / Signup Toggle Segments
            Card(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (!isSignUp) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable { 
                                isSignUp = false 
                                authErrorMessage = null
                                nameError = false
                                phoneError = false
                                passwordError = false
                            }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sign In".translate(),
                            fontWeight = FontWeight.Bold,
                            color = if (!isSignUp) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSignUp) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable { 
                                isSignUp = true 
                                authErrorMessage = null
                                nameError = false
                                phoneError = false
                                passwordError = false
                            }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sign Up".translate(),
                            fontWeight = FontWeight.Bold,
                            color = if (isSignUp) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Form inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Name Field (Only on Sign Up)
                    AnimatedVisibility(visible = isSignUp) {
                        Column {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    nameError = false
                                },
                                label = { Text("Full Name".translate()) },
                                isError = nameError,
                                supportingText = { if (nameError) Text("Name is required".translate(), color = Color.Red) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Phone Number Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            phoneError = false
                            authErrorMessage = null
                        },
                        label = { Text("Phone Number".translate()) },
                        isError = phoneError,
                        supportingText = { if (phoneError) Text("Enter a valid phone number".translate(), color = Color.Red) },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                            authErrorMessage = null
                        },
                        label = { Text("PIN / Password".translate()) },
                        isError = passwordError,
                        supportingText = { if (passwordError) Text("Must be at least 4 digits".translate(), color = Color.Red) },
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Text(if (isPasswordVisible) "👁️" else "🙈")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Error Message Banner
                    AnimatedVisibility(visible = authErrorMessage != null) {
                        authErrorMessage?.let { msg ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8E8)),
                                border = BorderStroke(1.dp, Color(0xFFF8B4B4)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("⚠️", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = msg.translate(),
                                        color = Color(0xFF9B1C1C),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            authErrorMessage = null
                            var hasError = false
                            if (isSignUp && name.isBlank()) {
                                nameError = true
                                hasError = true
                            }
                            if (phone.trim().length < 10) {
                                phoneError = true
                                hasError = true
                            }
                            if (password.trim().length < 4) {
                                passwordError = true
                                hasError = true
                            }

                            if (!hasError) {
                                isAuthenticating = true
                                coroutineScope.launch {
                                    delay(1500) // Mock authentication network delay
                                    val inputPhone = phone.trim()
                                    val inputPin = password.trim()

                                    if (isSignUp) {
                                        val existingUser = AppState.registeredUsers.find { it.phone == inputPhone }
                                        if (existingUser != null) {
                                            authErrorMessage = "An account with this phone number is already registered!"
                                            isAuthenticating = false
                                        } else {
                                            val newUser = com.nammamela.core.theme.UserAccount(name.trim(), inputPhone, inputPin)
                                            AppState.registeredUsers.add(newUser)
                                            AppState.loggedInUser = newUser.name
                                            AppState.loggedInPhone = "+91 " + newUser.phone
                                            AppState.isLoggedIn = true
                                            isAuthenticating = false
                                            onAuthSuccess()
                                        }
                                    } else {
                                        val registeredUser = AppState.registeredUsers.find { it.phone == inputPhone }
                                        if (registeredUser == null) {
                                            authErrorMessage = "Unregistered user. Please Sign Up to use the app."
                                            isAuthenticating = false
                                        } else if (registeredUser.pin != inputPin) {
                                            authErrorMessage = "Invalid password/PIN! Please try again."
                                            isAuthenticating = false
                                        } else {
                                            AppState.loggedInUser = registeredUser.name
                                            AppState.loggedInPhone = "+91 " + registeredUser.phone
                                            AppState.isLoggedIn = true
                                            isAuthenticating = false
                                            onAuthSuccess()
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !isAuthenticating,
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        if (isAuthenticating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = if (isSignUp) "Create Secure Account".translate() else "Authenticate Securely".translate(),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
