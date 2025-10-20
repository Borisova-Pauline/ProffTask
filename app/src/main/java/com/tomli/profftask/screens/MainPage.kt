package com.tomli.profftask.screens

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.databases.UserData
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.tomli.profftask.checkAndRequestPermission
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.FieldBackColorLight
import com.tomli.profftask.ui.theme.GreenRight
import com.tomli.profftask.ui.theme.OrangeApp
import com.tomli.profftask.ui.theme.RedPinkColor

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainPage(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    var user = remember { mutableStateOf(UserData(0, "", "","","","", 0,0, null))}
    var userList = proffViewModel.threeBestUsers.collectAsState(initial = emptyList())
    //var user = remember { mutableStateOf(UserData(0, "Polina@gmail", "Polina","Bor","1234","Russian", 1,2,null))}
    //var userList= listOf(user.value, user.value, user.value)
    proffViewModel.getUser(currentUserId, {usr -> user.value=usr})
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    checkAndRequestPermission(context)
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(bottom = down)) {
        Column(modifier = Modifier.background(PurpleApp).padding(top = up, start=25.dp, end =25.dp).fillMaxWidth()
            .clickable { navController.navigate("profile_screen") }) {
            Spacer(modifier = Modifier.height(7.dp))
            val b: Bitmap? = byteArrayToBitmap(user.value.image_uri)
            AsyncImage(model = b ?: R.mipmap.example_icon_user, contentDescription = null,
                modifier = Modifier.size(70.dp).clip(CircleShape))
            Text(text = "Hello, ${user.value.name}", color=Color.White, fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 7.dp))
            Text(text = "Are you ready for learning today?", color=Color(0xffb6b6b9), fontSize = 19.sp,
                modifier = Modifier.padding(vertical = 7.dp))
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)){
            Text(text = "Top users", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 7.dp))
            LazyColumn{
                items(userList.value){ item ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp))){
                        Box(modifier = Modifier.padding(10.dp)){
                            val a: Bitmap? = byteArrayToBitmap(item.image_uri)
                            AsyncImage(model = a ?: R.mipmap.example_icon_user, contentDescription = null,
                                modifier = Modifier.size(50.dp).clip(CircleShape))
                        }
                        Text(text = "${item.name}", fontSize=18.sp, modifier = Modifier.weight(1f).align(Alignment.CenterVertically).padding(5.dp),
                            maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onBackground)
                        Text(text = "${(item.guess_animal_count!!+item.right_choice_count!!)} points", fontSize=18.sp, modifier = Modifier.padding(15.dp).align(Alignment.CenterVertically), color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Text(text = "Available excersises", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 7.dp))
            Row{
                Column(modifier = Modifier.weight(1f).background(color = BlueButtonColor, shape = RoundedCornerShape(20.dp)).clickable { navController.navigate("guess_animal_screen") }){
                    Text(text = "\uD83D\uDC3B\u200D❄\uFE0F", fontSize = 70.sp, modifier = Modifier.fillMaxWidth().padding(top=8.dp), textAlign = TextAlign.Center)
                    Text(text = "Guess the animal", modifier = Modifier.fillMaxWidth().padding(bottom =8.dp), textAlign = TextAlign.Center, color = Color.White)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f).background(color = RedPinkColor, shape = RoundedCornerShape(20.dp)).clickable { navController.navigate("choose_word_screen") }){
                    Text(text = "✏\uFE0F", fontSize = 70.sp, modifier = Modifier.fillMaxWidth().padding(top=8.dp), textAlign = TextAlign.Center)
                    Text(text = "Word practice", modifier = Modifier.fillMaxWidth().padding(bottom=8.dp), textAlign = TextAlign.Center, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row{
                Column(modifier = Modifier.weight(1f).background(color = OrangeApp, shape = RoundedCornerShape(20.dp)).clickable { navController.navigate("audio_screen") }){
                    Text(text = "\uD83D\uDD0A", fontSize = 70.sp, modifier = Modifier.fillMaxWidth().padding(top=8.dp), textAlign = TextAlign.Center)
                    Text(text = "Audition", modifier = Modifier.fillMaxWidth().padding(bottom =8.dp), textAlign = TextAlign.Center, color = Color.White)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f).background(color = GreenRight, shape = RoundedCornerShape(20.dp)).clickable { navController.navigate("game_screen") }){
                    Text(text = "\uD83C\uDFAE", fontSize = 70.sp, modifier = Modifier.fillMaxWidth().padding(top=8.dp), textAlign = TextAlign.Center)
                    Text(text = "Game", modifier = Modifier.fillMaxWidth().padding(bottom=8.dp), textAlign = TextAlign.Center, color = Color.White)
                }
            }
        }
    }
    //Text(text = "${user.value}")
}



/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProffTaskTheme {
        MainPage(navController = rememberNavController())
    }
}*/