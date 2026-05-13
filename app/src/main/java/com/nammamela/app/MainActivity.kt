package com.nammamela.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nammamela.core.theme.NammaMelaTheme
import com.nammamela.core.theme.AppState
import com.nammamela.core.theme.translate
import com.nammamela.core.theme.AppLanguage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.nammamela.feature.booking.BookingConfirmationScreen
import com.nammamela.feature.fanwall.FanWallScreen
import com.nammamela.feature.home.HomeScreen
import com.nammamela.feature.home.VenuesScreen
import com.nammamela.feature.profile.ProfileScreen
import com.nammamela.feature.profile.AuthScreen
import com.nammamela.feature.seatmap.SeatMapScreen
import com.nammamela.feature.showdetail.ShowDetailScreen
import com.nammamela.feature.splash.SplashScreen
import com.nammamela.feature.studio.StudioScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaMelaTheme {
                val navController = rememberNavController()
                NammaMelaAppScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NammaMelaAppScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
 
    val bottomBarRoutes = listOf("home", "venues", "studio", "profile")
    val showBottomBar = currentRoute in bottomBarRoutes
 
    Scaffold(
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = {
                        AppState.currentLanguage = if (AppState.currentLanguage == AppLanguage.ENGLISH) {
                            AppLanguage.KANNADA
                        } else {
                            AppLanguage.ENGLISH
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = androidx.compose.foundation.shape.CircleShape,
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                ) {
                    Text(
                        text = if (AppState.currentLanguage == AppLanguage.ENGLISH) "ಕನ್ನಡ" else "EN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Text("🏠") },
                        label = { Text("Home".translate()) },
                        selected = currentRoute == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Text("📍") },
                        label = { Text("Maps".translate()) },
                        selected = currentRoute == "venues",
                        onClick = {
                            navController.navigate("venues") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Text("🎭") },
                        label = { Text("Studio".translate()) },
                        selected = currentRoute == "studio",
                        onClick = {
                            navController.navigate("studio") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Text("👤") },
                        label = { Text("Profile".translate()) },
                        selected = currentRoute == "profile",
                        onClick = {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") {
                SplashScreen(onSplashComplete = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("home") {
                HomeScreen(onShowSelected = { showId ->
                    navController.navigate("showDetail/$showId")
                })
            }
            composable("showDetail/{showId}") { backStackEntry ->
                val showId = backStackEntry.arguments?.getString("showId") ?: ""
                ShowDetailScreen(
                    showId = showId,
                    onBookSeatsClick = { id -> navController.navigate("seatMap/$id") },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("seatMap/{showId}") { backStackEntry ->
                val showId = backStackEntry.arguments?.getString("showId") ?: ""
                if (!AppState.isLoggedIn) {
                    AuthScreen(onAuthSuccess = {
                        navController.navigate("seatMap/$showId") {
                            popUpTo("seatMap/$showId") { inclusive = true }
                        }
                    })
                } else {
                    SeatMapScreen(
                        showId = showId,
                        onConfirmBooking = { seats ->
                            navController.navigate("bookingConfirmation/B${System.currentTimeMillis().toString().takeLast(4)}")
                        }
                    )
                }
            }
            composable("bookingConfirmation/{bookingId}") { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                BookingConfirmationScreen(
                    bookingId = bookingId,
                    onBackToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
            composable("studio") {
                if (!AppState.isLoggedIn) {
                    AuthScreen(onAuthSuccess = {
                        navController.navigate("studio") {
                            popUpTo("studio") { inclusive = true }
                        }
                    })
                } else {
                    StudioScreen()
                }
            }
            composable("venues") {
                VenuesScreen()
            }
            composable("fanWall/{showId}") { backStackEntry ->
                val showId = backStackEntry.arguments?.getString("showId") ?: ""
                FanWallScreen(showId = showId)
            }
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}
