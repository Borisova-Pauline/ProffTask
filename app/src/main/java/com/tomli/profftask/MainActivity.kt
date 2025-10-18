package com.tomli.profftask

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tomli.profftask.screens.ChooseRightScreen
import com.tomli.profftask.screens.GuessAnimal
import com.tomli.profftask.screens.LanguageSelect
import com.tomli.profftask.screens.LogIn
import com.tomli.profftask.screens.MainPage
import com.tomli.profftask.screens.OnboardingScreen
import com.tomli.profftask.screens.ProfileScreen
import com.tomli.profftask.screens.ResizeImage
import com.tomli.profftask.screens.SignUpAccount
import com.tomli.profftask.screens.SignUpPassword
import com.tomli.profftask.ui.theme.ProffTaskTheme


class MainActivity : ComponentActivity() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            var darkTheme = remember { mutableStateOf(sharedPrefs.getBoolean("isDarkTheme", false))}
            ProffTaskTheme(darkTheme = if(sharedPrefs.getBoolean("changeThemeAgreed", false)){darkTheme.value}else{ isSystemInDarkTheme()}) {
                ComposeNavigation({darkTheme.value=!darkTheme.value
                    sharedPrefs.edit().putBoolean("changeThemeAgreed", true).apply()
                    if(sharedPrefs.getBoolean("isDarkTheme", false)){
                        sharedPrefs.edit().putBoolean("isDarkTheme", false).apply()
                    }else{
                        sharedPrefs.edit().putBoolean("isDarkTheme", true).apply()
                    }})
            }
        }


    }

    override fun onStop() {
        super.onStop()

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerIntent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            triggerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = System.currentTimeMillis() + 10000
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ComposeNavigation(onThemeChange:()->Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var isUserInSystem = sharedPrefs.getBoolean("login", false)
    NavHost(
        navController = navController,
        startDestination = if(!isUserInSystem){"onboarding"}else{"main_page"}
    ) {
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("language_select") {
            LanguageSelect(navController)
        }
        composable("login") {
            LogIn(navController)
        }
        composable("signup_account") {
            SignUpAccount(navController)
        }
        composable("signup_password/{name}/{lastName}/{email}", arguments = listOf(navArgument("name") {type = NavType.StringType},
            navArgument("lastName") {type = NavType.StringType}, navArgument("email"){type=
                NavType.StringType})){
                navBackStack ->  val name: String = navBackStack.arguments?.getString("name") ?: "user"
            val lastName: String = navBackStack.arguments?.getString("lastName") ?: "userovich"
            val email: String = navBackStack.arguments?.getString("email") ?: "example@gmail.com"

            SignUpPassword(navController, name, lastName, email)
        }
        composable("main_page") {
            MainPage(navController)
        }
        composable("profile_screen") {
            ProfileScreen(navController, onThemeChange=onThemeChange)
        }
        composable("choose_word_screen") {
            ChooseRightScreen(navController)
        }
        composable("guess_animal_screen") {
            GuessAnimal(navController)
        }
        composable("resize_image_screen") {
            ResizeImage(navController)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Моя смерть",
        modifier = modifier
    )
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProffTaskTheme {
        Greeting("Android")
    }
}*/