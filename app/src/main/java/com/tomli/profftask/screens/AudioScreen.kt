package com.tomli.profftask.screens

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.tomli.profftask.R
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.GreenBlueColor
import com.tomli.profftask.ui.theme.GreenRight
import com.tomli.profftask.ui.theme.PurpleApp
import com.tomli.profftask.ui.theme.RedPinkColor
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AudioScreen(navController: NavController){
    val context = LocalContext.current
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val word = remember { mutableStateOf(Random.nextInt(0, enumValues<Words>().last().ordinal + 1)) }

    val isSpeech = remember { mutableStateOf(false) }
    val isNext = remember { mutableStateOf(false) }

    val yourWordThis = remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(bottom = down)) {
        Box(modifier = Modifier
            .background(PurpleApp)
            .fillMaxWidth()
            .padding(15.dp)) {
            Column{
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Listening",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier
                    .padding(top = up)
                    .size(20.dp)
                    .clickable { navController.navigate("main_page") })
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = Words.entries.get(word.value).name, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp), textAlign = TextAlign.Center, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(text = Words.entries.get(word.value).engWordTranscription, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text="Please press button and say this word. Our service will check your pronunciation",
                modifier = Modifier.padding(vertical = 15.dp), fontWeight = FontWeight.Bold)
            if(!isSpeech.value && !isNext.value){
                Button(onClick = {
                    checkAndRequestPermissionAUDIO(context)
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED){
                        isSpeech.value=true
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text="Check my speech", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                }
            }
            var answerColor: Color
            if(isNext.value){
                answerColor= GreenRight
            }else{answerColor= RedPinkColor}

            if(isSpeech.value){
                Text("Your result")
                OutlinedTextField(value = yourWordThis.value, onValueChange = {newText -> yourWordThis.value = newText},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedTextColor = answerColor, focusedTextColor = answerColor, disabledTextColor = answerColor
                    ), readOnly = true)
                if(!isNext.value){
                    SpeechToTextComposable({yourWord -> yourWordThis.value=yourWord }, context)
                }else{
                    Button(onClick = {
                        word.value=Random.nextInt(0, enumValues<Words>().last().ordinal + 1)
                        yourWordThis.value=""
                        isSpeech.value=false
                        isNext.value=false
                    }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 25.dp), shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text="Yay! Go next", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
                    }
                }
            }
            if(yourWordThis.value.equals(Words.entries.get(word.value).name, ignoreCase = true)){
                isNext.value=true
            }
        }
    }
}


@Composable
fun SpeechToTextComposable(onReturn:(yourWord: String)->Unit, context: Context) {
    var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
    var recognizedText = remember { mutableStateOf("") }
    var isListening= remember { mutableStateOf(false) }

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechRecognitionListener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening.value = true
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                recognizedText.value = matches?.get(0) ?: ""
                isListening.value = false
                while(recognizedText.value==""){ }
                onReturn(recognizedText.value)
            }

            override fun onError(error: Int) {
                isListening.value = false
            }

            override fun onBeginningOfSpeech() {
            }
            override fun onRmsChanged(p0: Float) {
                val quiet_max = 25f
                val quiet_min = 65f
            }
            override fun onBufferReceived(p0: ByteArray?) {
            }
            override fun onEndOfSpeech() {
                isListening.value=false
            }
            override fun onPartialResults(p0: Bundle?) {
                val matches = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                recognizedText.value = matches?.get(0) ?: ""
            }
            override fun onEvent(p0: Int, p1: Bundle?) {
            }
        }
    }
    speechRecognizer.setRecognitionListener(speechRecognitionListener)
    Column(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.padding(40.dp)
            .background(color = GreenBlueColor, shape = (RoundedCornerShape(35.dp)))
            .clickable { speechRecognizer.startListening(intent) }
            .align(Alignment.CenterHorizontally)){
            Text(text="\uD83C\uDF99\uFE0F", fontSize = 120.sp)
        }
    }
}


val AUDIO_PERMISSION_CODE = 100
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun checkAndRequestPermissionAUDIO(context: Context){
    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            AUDIO_PERMISSION_CODE
        )
    }
}