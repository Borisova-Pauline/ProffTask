package com.tomli.profftask.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomli.profftask.Greeting
import com.tomli.profftask.R
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.MyColorsTheme
import com.tomli.profftask.ui.theme.OrangeApp
import com.tomli.profftask.ui.theme.PaleOrangeApp
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp

@Composable
fun OnboardingScreen(navController: NavController){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var currentPage = remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top=up, bottom=down)){
        when(currentPage.value){
            0-> OnboardingPage(R.drawable.onboarding0, {currentPage.value++}, currentPage.value,{navController.navigate("language_select")})
            1-> OnboardingPage(R.drawable.onboarding1, {currentPage.value++}, currentPage.value, {navController.navigate("language_select")})
            2-> OnboardingPage(R.drawable.onboarding2, {navController.navigate("language_select")}, currentPage.value, {navController.navigate("language_select")})
        }
    }
}

@Composable
fun OnboardingPage(picId: Int, ButtonClick:()-> Unit, pageNum: Int, onSkip:()->Unit){
    var listColors = remember { mutableListOf(Color.Gray, Color.Gray, Color.Gray) }
    listColors[pageNum]=OrangeApp
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)){
        Box(modifier = Modifier.weight(1f).fillMaxWidth()){
            Image(painter = painterResource(picId), contentDescription = null,
                modifier = Modifier.align(Alignment.Center))
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(35.dp)){
            Canvas(modifier = Modifier){
                drawCircle(color = listColors[0], radius = 15f)
            }
            Canvas(modifier = Modifier.padding(horizontal = 25.dp)){
                drawCircle(color = listColors[1], radius = 15f)
            }
            Canvas(modifier = Modifier){
                drawCircle(color = listColors[2], radius = 15f)
            }
        }
        var phrase1: String
        var phrase2: String
        var buttonText: String
        when(pageNum){
            0->{
                phrase1="Confidence in your words"
                phrase2="With conversation-based learning, you'll be talking from lesson one"
                buttonText="Next"
            }
            1->{
                phrase1="Take your time to learn"
                phrase2="Develop a habit of learning and make it a part of your daily routine"
                buttonText="More"
            }
            2->{
                phrase1="The lessons you need to learn"
                phrase2="Using a variety of learning styles to learn and retain"
                buttonText="Choose a language"
            }
            else->{
                phrase1=""
                phrase2=""
                buttonText=""
            }
        }
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)){
            Text(text=phrase1, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text(text=phrase2, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0xff7e8188))
        }
        Button(onClick = ButtonClick, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), shape = RoundedCornerShape(15.dp)
        ) {
            Text(text=buttonText, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
        }
        Text(text="Skip onboarding", modifier = Modifier.fillMaxWidth().clickable { onSkip() }, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))
    }
}


@Composable
fun LanguageSelect(navController: NavController){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val context = LocalContext.current
    val choiceLanguage= remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)){
        Column(modifier = Modifier.background(PurpleApp).fillMaxWidth()){
            Spacer(modifier = Modifier.height(up))
            Text(text="Language select", color = Color.White,modifier = Modifier.fillMaxWidth().padding(15.dp), textAlign = TextAlign.Center, fontSize = 20.sp)
        }
        Text(text="What is your Mother language?", modifier = Modifier.fillMaxWidth().padding(15.dp), textAlign = TextAlign.Center, fontSize = 20.sp)
        ButtonLanguage("Russian", choiceLanguage.value, 0, {choiceLanguage.value=0})
        ButtonLanguage("English", choiceLanguage.value, 1, {choiceLanguage.value=1})
        ButtonLanguage("Chinese", choiceLanguage.value, 2, {choiceLanguage.value=2})
        ButtonLanguage("Belarus", choiceLanguage.value, 3, {choiceLanguage.value=3})
        ButtonLanguage("Kazakh", choiceLanguage.value, 4, {choiceLanguage.value=4})
        Box(modifier = Modifier.weight(1f)){
            Button(onClick = {
                if(choiceLanguage.value==0){
                    navController.navigate("login")
                }else{
                    Toast.makeText(context, "Nope", Toast.LENGTH_LONG).show()
                }
                             }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).align(Alignment.BottomCenter).padding(horizontal = 10.dp, vertical = 7.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Choose", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun ButtonLanguage(name: String, chocenButton: Int, thisButton: Int, onChange:()-> Unit){
    var backColor: Color
    if(thisButton==chocenButton){
        backColor=OrangeApp
    }else{
        backColor=PaleOrangeApp
    }
    Box(modifier = Modifier.padding(10.dp).background(backColor, shape = RoundedCornerShape(15.dp)).fillMaxWidth().clickable { onChange() }){
        Text(name, modifier = Modifier.padding(15.dp), fontSize = 20.sp)
    }
}
