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
import com.nammamela.feature.booking.BookingConfirmationScreen
import com.nammamela.feature.fanwall.FanWallScreen
import com.nammamela.feature.home.HomeScreen
import com.nammamela.feature.profile.ProfileScreen
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

    val bottomBarRoutes = listOf("home", "studio", "profile")
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Text("🏠") },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Text("🎭") },
                        label = { Text("Studio") },
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
                        label = { Text("Profile") },
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
                SeatMapScreen(
                    showId = showId,
                    onConfirmBooking = { seats ->
                        // In real app, pass serialized data or use shared ViewModel
                        navController.navigate("bookingConfirmation/B${System.currentTimeMillis().toString().takeLast(4)}")
                    }
                )
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
                StudioScreen()
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
