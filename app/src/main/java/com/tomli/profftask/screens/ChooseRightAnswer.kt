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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.GreenRight
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp
import com.tomli.profftask.ui.theme.RedPinkColor
import kotlin.random.Random

@Composable
fun ChooseRightScreen(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val numsList = remember { mutableStateOf(generateFourNums()) }
    val word = remember { mutableStateOf(numsList.value[Random.nextInt(0, 3)])}
    val chosenVar = remember { mutableStateOf(-1) }
    var checkAnswer = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(bottom = down)) {
        Box(modifier = Modifier.background(PurpleApp).fillMaxWidth().padding(15.dp)) {
            Column{
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Word practice",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier.padding(top=up).size(20.dp).clickable { navController.navigate("main_page") })
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = Words.entries.get(word.value).name, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), textAlign = TextAlign.Center, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(text = Words.entries.get(word.value).engWordTranscription, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(numsList.value){ item ->
                    if(!checkAnswer.value){
                        var color: Color
                        var wordColor: Color
                        if(item==chosenVar.value){
                            color = BlueButtonColor
                            wordColor = Color.White
                        }else{
                            color = Color(0xffe5e5e5)
                            wordColor = Color.Black
                        }
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).background(color = color, shape = RoundedCornerShape(15.dp))
                            .clickable { chosenVar.value=item }){
                            Text(text = Words.entries.get(item).rusWord, modifier = Modifier.fillMaxWidth().padding(10.dp), textAlign = TextAlign.Center, fontSize = 18.sp, color=wordColor)
                        }
                    }else{
                        var color: Color
                        var wordColor: Color
                        if(item==word.value){
                            color = GreenRight
                            wordColor = Color.White
                        }else if(item!=word.value && item==chosenVar.value){
                            color= RedPinkColor
                            wordColor = Color.White
                        }else{
                            color = Color(0xffe5e5e5)
                            wordColor = Color.Black
                        }
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).background(color = color, shape = RoundedCornerShape(15.dp))){
                            Text(text = Words.entries.get(item).rusWord, modifier = Modifier.fillMaxWidth().padding(10.dp), textAlign = TextAlign.Center, fontSize = 18.sp, color = wordColor)
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if(!checkAnswer.value){
                Button(onClick = {
                    if(chosenVar.value>-1){
                        checkAnswer.value=true
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text="Check", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                }
            }else{
                Button(onClick = {
                    if(word.value==chosenVar.value){
                        proffViewModel.setNewRightChoiceCount(currentUserId, 1)
                    }else{
                        proffViewModel.setNewRightChoiceCount(currentUserId, 0)
                    }
                    checkAnswer.value=false
                    numsList.value = generateFourNums()
                    word.value = numsList.value[Random.nextInt(0, 3)]
                    chosenVar.value = -1
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text="Next", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                }
            }
        }

    }
}

fun generateFourNums(): List<Int>{
    val max = enumValues<Words>().last().ordinal + 1
    var first =Random.nextInt(0, max)
    var second: Int
    var third: Int
    var fourth: Int
    do{
        second= Random.nextInt(0, max)
    }while(first==second)
    do{
        third= Random.nextInt(0, max)
    }while(first==third || second==third)
    do{
        fourth= Random.nextInt(0, max)
    }while(first==fourth || second==fourth || third==fourth)
    return listOf(first, second, third, fourth)
}

enum class Words(val engWordTranscription: String, val rusWord: String){
    Gardener("[ˈɡɑːdnə]", "Cадовник"),
    Fly("[flaɪ]", "Муха"),
    Dog("[dɒɡ]","Собака"),
    Gladiolus("[ˌɡlædɪˈəʊləs]", "Гладиолус")
}



