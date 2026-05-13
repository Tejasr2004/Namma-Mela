package com.nammamela.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppLanguage { ENGLISH, KANNADA }

data class UserAccount(val name: String, val phone: String, val pin: String)

// Globally shared, fully reactive app configuration state
object AppState {
    var isDarkMode by mutableStateOf(true) // Start with dark theme as premium cinematic default
    var isLoggedIn by mutableStateOf(false) // Start logged out so the user sees the secure login portal!
    var loggedInUser by mutableStateOf<String?>(null)
    var loggedInPhone by mutableStateOf<String?>(null)
    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)
    
    // In-memory database registry of registered accounts (pre-seeded with demo accounts)
    val registeredUsers = mutableListOf(
        UserAccount("Ramesh Gowda", "9876543210", "1234"),
        UserAccount("Tejas Gowda", "9900881122", "4321")
    )
}

fun String.translate(): String {
    return if (AppState.currentLanguage == AppLanguage.KANNADA) {
        when (this) {
            "Home" -> "ಮನೆ"
            "Maps" -> "ಸ್ಥಳಗಳು"
            "Studio" -> "ಸ್ಟುಡಿಯೋ"
            "Profile" -> "ಪ್ರೊಫೈಲ್"
            
            "Namma Mela" -> "ನಮ್ಮ ಮೇಳ"
            "Featured Shows" -> "ವಿಶೇಷ ಮೇಳಗಳು"
            "Browse Productions" -> "ಇತರ ಪ್ರದರ್ಶನಗಳು"
            "Secure Ticket" -> "ಸುರಕ್ಷಿತ ಟಿಕೆಟ್"
            "Logout" -> "ಲಾಗ್ ಔಟ್"
            
            "Welcome Back" -> "ಸ್ವಾಗತ"
            "Create Account" -> "ಖಾತೆಯನ್ನು ರಚಿಸಿ"
            "Sign In" -> "ಸೈನ್ ಇನ್"
            "Sign Up" -> "ಸೈನ್ ಅಪ್"
            "Phone Number" -> "ದೂರವಾಣಿ ಸಂಖ್ಯೆ"
            "PIN / Password" -> "ಪಿನ್ / ಪಾಸ್‌ವರ್ಡ್"
            "Authenticate Securely" -> "ಸುರಕ್ಷಿತವಾಗಿ ಲಾಗಿನ್ ಮಾಡಿ"
            "Create Secure Account" -> "ಸುರಕ್ಷಿತ ಖಾತೆಯನ್ನು ರಚಿಸಿ"
            "Full Name" -> "ಪೂರ್ಣ ಹೆಸರು"
            "Phone Number is required" -> "ದೂರವಾಣಿ ಸಂಖ್ಯೆ ಕಡ್ಡಾಯ"
            "PIN / Password is required" -> "ಪಿನ್ ಕಡ್ಡಾಯ"
            
            "Sri Manjunatha Troupe" -> "ಶ್ರೀ ಮಂಜುನಾಥ ಮೇಳ"
            "Veerabhadreshwara Krupa" -> "ವೀರಭದ್ರೇಶ್ವರ ಕೃಪಾ ಮೇಳ"
            "Gubbi Veeranna Nataka Company" -> "ಗುಬ್ಬಿ ವೀರಣ್ಣ ನಾಟಕ ಕಂಪನಿ"
            "Sri Durga Devi Prasanna Sabha" -> "ಶ್ರೀ ದುರ್ಗಾದೇವಿ ಪ್ರಸನ್ನ ಸಭಾ"
            "Saligrama Yakshagana Mandali" -> "ಸಾಲಿಗ್ರಾಮ ಯಕ್ಷಗಾನ ಮಂಡಳಿ"
            
            "Kurukshetra" -> "ಕುರುಕ್ಷೇತ್ರ"
            "Rakta Ratri" -> "ರಕ್ತ ರಾತ್ರಿ"
            "Dakshayagna" -> "ದಕ್ಷಯಜ್ಞ"
            "Shanishwara Mahatme" -> "ಶನೀಶ್ವರ ಮಹಾತ್ಮೆ"
            "Prahlada Charitre" -> "ಪ್ರಹ್ಲಾದ ಚರಿತ್ರೆ"
            
            "Sign in securely to manage bookings and troupe" -> "ಬುಕಿಂಗ್ ಮತ್ತು ಮೇಳ ನಿರ್ವಹಣೆಗೆ ಸುರಕ್ಷಿತವಾಗಿ ಲಾಗಿನ್ ಮಾಡಿ"
            "Sign up to book nomadic mela seats safely" -> "ಮೇಳ ಆಸನಗಳನ್ನು ಸುರಕ್ಷಿತವಾಗಿ ಬುಕ್ ಮಾಡಲು ಖಾತೆ ರಚಿಸಿ"
            "Unregistered user. Please Sign Up to use the app." -> "ನೋಂದಾಯಿಸದ ಬಳಕೆದಾರ. ದಯವಿಟ್ಟು ಸೈನ್ ಅಪ್ ಮಾಡಿ."
            "Invalid password/PIN! Please try again." -> "ತಪ್ಪಾದ ಪಿನ್! ದಯವಿಟ್ಟು ಮತ್ತೊಮ್ಮೆ ಪ್ರಯತ್ನಿಸಿ."
            "An account with this phone number is already registered!" -> "ಈ ಸಂಖ್ಯೆಯ ಖಾತೆ ಈಗಾಗಲೇ ನೋಂದಾಯಿಸಲ್ಪಟ್ಟಿದೆ!"
            
            "Preferences" -> "ಆದ್ಯತೆಗಳು"
            "🌓 Dark Mode Theme" -> "🌓 ಡಾರ್ಕ್ ಮೋಡ್ ಥೀಮ್"
            "Switch between Dark and Light skins" -> "ಡಾರ್ಕ್ ಮತ್ತು ಲೈಟ್ ಮೋಡ್‌ಗಳ ನಡುವೆ ಬದಲಾಯಿಸಿ"
            "🔒 Log Out Securely" -> "🔒 ಸುರಕ್ಷಿತವಾಗಿ ಲಾಗ್ ಔಟ್ ಮಾಡಿ"
            "My Tickets & History" -> "ನನ್ನ ಟಿಕೆಟ್‌ಗಳು ಮತ್ತು ಇತಿಹಾಸ"
            "DATE & TIME" -> "ದಿನಾಂಕ ಮತ್ತು ಸಮಯ"
            "DATE" -> "ದಿನಾಂಕ"
            "SEATS" -> "ಆಸನಗಳು"
            "Dismiss ticket" -> "ಟಿಕೆಟ್ ಮುಚ್ಚಿ"
            "Today" -> "ಇಂದು"
            "🎟️ DIGITAL ADMISSION PASS" -> "🎟️ ಸುರಕ್ಷಿತ ಡಿಜಿಟಲ್ ಪ್ರವೇಶ ಪತ್ರ"
            "BOOKING ID" -> "ಬುಕಿಂಗ್ ಸಂಖ್ಯೆ"
            "SEATS REG" -> "ಆಸನಗಳು"
            
            "Nomadic Tents" -> "ಸಂಚಾರಿ ಮೇಳಗಳು"
            "Live touring locations of folk theaters and bayalata tents" -> "ಲೈವ್ ಜನಪದ ನಾಟಕ ಮತ್ತು ಬಯಲಾಟ ಸಂಚಾರಿ ಸ್ಥಳಗಳು"
            "● TOURING NOW" -> "● ಪ್ರದರ್ಶನ ನಡೆಯುತ್ತಿದೆ"
            "⏱️ NEXT WEEK" -> "⏱️ ಮುಂದಿನ ವಾರ"
            "📅 UPCOMING" -> "📅 ಮುಂಬರುವ"
            "FIELD / VILLAGE" -> "ಸ್ಥಳ / ಗ್ರಾಮ"
            "DATES" -> "ದಿನಾಂಕಗಳು"
            "Route to Tent Field (GPS)" -> "ಸ್ಥಳಕ್ಕೆ ದಾರಿ ತೋರಿಸು (GPS)"
            "Sri Siddharoodha Tent" -> "ಶ್ರೀ ಸಿದ್ದಾರೂಢ ಬಯಲಾಟ ಮೇಳ"
            "Shakambhari Nataka Mandali" -> "ಶಾಕಂಭರಿ ನಾಟಕ ಮಂಡಳಿ"
            "Kranthi Raitha Sangha" -> "ಕ್ರಾಂತಿ ರೈತ ಸಂಘ"
            "Sri Kalika Prasadita Mandali" -> "ಶ್ರೀ ಕಾಳಿಕಾ ಪ್ರಸಾದಿತ ಮಂಡಳಿ"
            "Kittur Chennamma Bayalata Troupe" -> "ಕಿತ್ತೂರು ಚೆನ್ನಮ್ಮ ಬಯಲಾಟ ಮೇಳ"
            
            "Above 80%" -> "೮೦% ಕ್ಕಿಂತ ಹೆಚ್ಚು"
            "75-80%" -> "೭೫-೮೦%"
            "70-75%" -> "೭೦-೭೫%"
            "60-70%" -> "೬೦-೭೦%"
            "below 60%" -> "೬೦% ಕ್ಕಿಂತ ಕಡಿಮೆ"
            "Mela Popularity Rate Of Karnataka" -> "ಕರ್ನಾಟಕ ಜಿಲ್ಲಾವಾರು ಮೇಳ ಜನಪ್ರಿಯತೆ ದರ"
            
            else -> this
        }
    } else {
        this
    }
}

private val AppleLightColorScheme = lightColorScheme(
    primary = SaffronAccent,
    secondary = TealAccent,
    tertiary = MarigoldAccent,
    background = AppleSystemGrey,
    surface = AppleWhite,
    onPrimary = AppleWhite,
    onSecondary = AppleWhite,
    onTertiary = AppleWhite,
    onBackground = AppleTextPrimary,
    onSurface = AppleTextPrimary,
)

private val AppleDarkColorScheme = darkColorScheme(
    primary = SaffronAccent,
    secondary = TealAccent,
    tertiary = MarigoldAccent,
    background = AppleDarkBg,
    surface = AppleDarkSurface,
    onPrimary = AppleWhite,
    onSecondary = AppleWhite,
    onTertiary = AppleWhite,
    onBackground = AppleDarkTextPrimary,
    onSurface = AppleDarkTextPrimary,
)

@Composable
fun NammaMelaTheme(
    darkTheme: Boolean = AppState.isDarkMode,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AppleDarkColorScheme else AppleLightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
