package com.tomli.profftask.screens

import android.content.Context
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.databases.UserData

@Composable
fun MainPage(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    Log.v("error", "$currentUserId")
    var user = remember { mutableStateOf(UserData(0, "", "","","","", 0,0))}
    proffViewModel.getUser(currentUserId, {usr -> user.value=usr})

    Text(text = "${user.value}")
}