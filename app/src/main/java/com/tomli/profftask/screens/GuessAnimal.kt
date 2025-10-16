package com.tomli.profftask.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.FieldBackColorLight
import com.tomli.profftask.ui.theme.GreenRight
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp
import com.tomli.profftask.ui.theme.RedPinkColor
import java.util.Locale
import kotlin.random.Random

@Composable
fun GuessAnimal(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val animalWrite = remember { mutableStateOf("") }
    val animal = remember{mutableStateOf(Random.nextInt(0, (enumValues<Words>().last().ordinal + 1)))}
    val colorUp = remember { mutableStateOf(PurpleApp) }
    val screenExercise = remember { mutableStateOf(ScreenStates.Exercise) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)) {
        Box(modifier = Modifier.background(colorUp.value).fillMaxWidth().padding(15.dp)) {
            Column{
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Guess the animal",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier.padding(top=up).size(20.dp).clickable { navController.navigate("main_page") })
        }
        when(screenExercise.value){
            ScreenStates.Exercise -> {
                Column(modifier = Modifier.padding(15.dp)){
                    Image(painter = painterResource(AnimalsGuess.entries.get(animal.value).image), contentDescription = null,
                        contentScale = ContentScale.FillWidth, modifier = Modifier.fillMaxWidth().clip(
                            RoundedCornerShape(20.dp)).heightIn(max = 500.dp))


                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text="Write who is on image")
                    OutlinedTextField(value = animalWrite.value, onValueChange = {newText -> animalWrite.value = newText},
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                            cursorColor = PurpleApp,
                            unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                        ))
                    Button(onClick = {
                        if(AnimalsGuess.entries.get(animal.value).nameAnimal == animalWrite.value.trim().toLowerCase(Locale.ROOT)){
                            proffViewModel.setNewGuessAnimalCount(currentUserId, 1)
                            colorUp.value = GreenRight
                            screenExercise.value = ScreenStates.RightAnswer
                        }else{
                            proffViewModel.setNewGuessAnimalCount(currentUserId, 0)
                            colorUp.value = RedPinkColor
                            screenExercise.value = ScreenStates.WrongAnswer
                        }
                    }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text="Check", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
                    }
                }
            }
            ScreenStates.RightAnswer -> {
                Column(modifier = Modifier.padding(15.dp)){
                    Text(text="\uD83C\uDF89", fontSize = 180.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(15.dp))
                    Text(text="Holy Molly! That is Right!", fontSize = 22.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        colorUp.value = PurpleApp
                        animal.value = Random.nextInt(0, (enumValues<Words>().last().ordinal + 1))
                        animalWrite.value = ""
                        screenExercise.value = ScreenStates.Exercise
                    }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 35.dp), shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text="Next", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
                    }
                }
            }
            ScreenStates.WrongAnswer -> {
                Column(modifier = Modifier.padding(15.dp)){
                    Text(text="\uD83D\uDE3F", fontSize = 180.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(15.dp))
                    Text(text="Eh? Wrong answer :(\nThat is: ${AnimalsGuess.entries.get(animal.value).name}", fontSize = 22.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        colorUp.value = PurpleApp
                        animal.value = Random.nextInt(0, (enumValues<Words>().last().ordinal + 1))
                        animalWrite.value = ""
                        screenExercise.value = ScreenStates.Exercise
                    }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 35.dp), shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text="Next", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
                    }
                    Button(onClick = {
                        colorUp.value = PurpleApp
                        animalWrite.value = ""
                        screenExercise.value = ScreenStates.Exercise
                    }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text="Try again", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
                    }
                }
            }
        }

    }
}

enum class ScreenStates{
    Exercise, RightAnswer, WrongAnswer
}


enum class AnimalsGuess(val nameAnimal: String, val image: Int){
    Raccoon("raccoon", R.drawable.photo_raccoon),
    Cat("cat", R.drawable.photo_cat),
    Dog("dog", R.drawable.photo_dog),
    Goat("goat", R.drawable.photo_goat),
    Fox("fox", R.drawable.photo_fox),
    Fly("fly", R.drawable.photo_fly)
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProffTaskTheme {
        GuessAnimal(navController = rememberNavController())
    }
}