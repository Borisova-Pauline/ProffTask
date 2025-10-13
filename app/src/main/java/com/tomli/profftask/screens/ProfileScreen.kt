package com.tomli.profftask.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.databases.UserData
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp

@Composable
fun ProfileScreen(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    var user = remember { mutableStateOf(UserData(0, "", "","","","", 0,0,null)) }
    //var user = remember { mutableStateOf(UserData(0, "Polina@gmail", "Polina","Bor","1234","Russian", 1,2,null))}
    proffViewModel.getUser(currentUserId, {usr -> user.value=usr})
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)) {
        Column(modifier = Modifier.background(PurpleApp).padding(top = up, start=25.dp, end =25.dp).fillMaxWidth()
            .clickable { navController.navigate("main_page") }) {
            Spacer(modifier = Modifier.height(7.dp))
            AsyncImage(model = user.value.image_uri ?: R.mipmap.example_icon_user, contentDescription = null,
                modifier = Modifier.size(120.dp).clip(CircleShape))
            Text(text = "Your profile, ${user.value.name}", color=Color.White, fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 9.dp))
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)){
            Spacer(modifier = Modifier.weight(1f).fillMaxWidth())
            Button(onClick = {
                Toast.makeText(context, "Uh Oh", Toast.LENGTH_LONG).show()
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Switch to Dark", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Button(onClick = { navController.navigate("language_select")
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Change mother language", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Button(onClick = {
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Change your image", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Button(onClick = {
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Logout", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}


