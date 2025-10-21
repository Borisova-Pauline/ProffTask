package com.tomli.profftask.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.GreenRight
import com.tomli.profftask.ui.theme.PurpleApp
import com.tomli.profftask.ui.theme.RedPinkColor
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavController){
    val context = LocalContext.current
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val numsList = remember { mutableStateOf(generateFourNums()) }
    val word = remember { mutableStateOf(numsList.value[Random.nextInt(0, 4)]) }
    val chosenVar = remember { mutableStateOf(-1) }
    var checkAnswer = remember { mutableStateOf(false) }

    var rounds = 7
    var currentRound = remember { mutableStateOf(1) }
    val timeOfThisRound = remember { mutableStateOf(15) }

    var computersChoice = remember { mutableStateOf(-1) }
    var isComputerAnswered = remember { mutableStateOf(false) }
    val timeComputer = remember { mutableStateOf(Random.nextInt(1, 3)) }


    LaunchedEffect(key1 = currentRound.value) {
        while(timeComputer.value>=0){
            delay(1000)
            timeComputer.value--
        }
    }
    //Timer(timeComputer.value, {timeComputer.value--}, true, currentRound.value, modifier = Modifier)
    if(timeComputer.value==0){
        computersChoice.value = makeComputerDecision(word.value)
        isComputerAnswered.value = true
    }
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
        Box(modifier = Modifier.fillMaxWidth()){
            LaunchedEffect(key1 = currentRound.value) {
                while(timeOfThisRound.value>0){
                    delay(1000)
                    timeOfThisRound.value--
                }
            }
            if(timeOfThisRound.value<0){
                timeOfThisRound.value=0
            }
            Text(text="Оставшееся время: ${timeOfThisRound.value}", modifier = Modifier.align(
                Alignment.Center).padding(5.dp))
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = Words.entries.get(word.value).name,
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = Words.entries.get(word.value).engWordTranscription,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(numsList.value) { item ->
                    if (!checkAnswer.value) {
                        var color: Color
                        var wordColor: Color
                        val who: String
                        if (item == chosenVar.value || item == computersChoice.value) {
                            color = BlueButtonColor
                            wordColor = Color.White
                        } else {
                            color = Color(0xffe5e5e5)
                            wordColor = Color.Black
                        }
                        if(item == chosenVar.value && item == computersChoice.value){
                            who = "You & P2"
                        }else if(item == chosenVar.value){
                            who = "You"
                        }else if(item == computersChoice.value){
                            who = "P2"
                        }else{
                            who = ""
                        }
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .background(color = color, shape = RoundedCornerShape(15.dp))
                            .clickable { chosenVar.value = item }) {
                            Text(
                                text = Words.entries.get(item).rusWord,
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = wordColor
                            )
                            Text(text = who,
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                fontSize = 18.sp,
                                color = wordColor)
                        }
                    } else {
                        var color: Color
                        var wordColor: Color
                        var who: String
                        if (item == word.value) {
                            color = GreenRight
                            wordColor = Color.White
                        } else if (item != word.value && (item == chosenVar.value||item==computersChoice.value)) {
                            color = RedPinkColor
                            wordColor = Color.White
                        } else {
                            color = Color(0xffe5e5e5)
                            wordColor = Color.Black
                        }

                        if(item == chosenVar.value && item == computersChoice.value){
                            who = "You & P2"
                        }else if(item == chosenVar.value){
                            who = "You"
                        }else if(item == computersChoice.value){
                            who = "P2"
                        }else{
                            who = ""
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                                .background(color = color, shape = RoundedCornerShape(15.dp))
                        ) {
                            Text(
                                text = Words.entries.get(item).rusWord,
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = wordColor
                            )
                            Text(text = who,
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                fontSize = 18.sp,
                                color = wordColor)
                        }
                    }

                }
            }
            if(timeOfThisRound.value<=0 && !checkAnswer.value){
                checkAnswer.value=true
                if(chosenVar.value<=0){
                    Toast.makeText(context, "Время вышло!", Toast.LENGTH_LONG).show()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if(!checkAnswer.value){
                Button(onClick = {
                    if(computersChoice.value<0){
                        Toast.makeText(context, "P2 ещё не выбрал", Toast.LENGTH_LONG).show()
                    }else{
                        if(chosenVar.value>-1){
                            checkAnswer.value=true
                            timeOfThisRound.value=0
                            //currentRound.value++
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text="Check", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                }
            }else{
                Button(onClick = {
                    if(currentRound.value==rounds){
                        navController.navigate("main_page")
                    }else{
                        checkAnswer.value=false
                        numsList.value = generateFourNums()
                        word.value = numsList.value[Random.nextInt(0, 3)]
                        chosenVar.value = -1
                        computersChoice.value = -1
                        isComputerAnswered.value = false
                        timeComputer.value = Random.nextInt(1, 3)
                        timeOfThisRound.value = 15
                        currentRound.value++
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                ) {
                    if(currentRound.value==rounds){
                        Text(text="Finish", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                    }else{
                        Text(text="Next", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                    }
                }
            }
        }
    }
}


fun makeComputerDecision(rightAnswer: Int): Int{
    val chance = Random.nextInt(1, 11)
    if(chance<=2){
        return rightAnswer
    }else{
        var answer: Int
        do{
            answer = Random.nextInt(0, 4)
        }while(answer==rightAnswer)
        return answer
    }
}