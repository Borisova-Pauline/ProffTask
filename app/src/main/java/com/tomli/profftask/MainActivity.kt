package com.tomli.profftask

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tomli.profftask.screens.ChooseRightScreen
import com.tomli.profftask.screens.LanguageSelect
import com.tomli.profftask.screens.LogIn
import com.tomli.profftask.screens.MainPage
import com.tomli.profftask.screens.OnboardingScreen
import com.tomli.profftask.screens.ProfileScreen
import com.tomli.profftask.screens.SignUpAccount
import com.tomli.profftask.screens.SignUpPassword
import com.tomli.profftask.ui.theme.ProffTaskTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProffTaskTheme {
                ComposeNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ComposeNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "onboarding"
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
            ProfileScreen(navController)
        }
        composable("choose_word_screen") {
            ChooseRightScreen(navController)
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