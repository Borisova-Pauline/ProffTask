package com.tomli.profftask.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProfileScreen(navController: NavController, onThemeChange:()-> Unit, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    var user = remember { mutableStateOf(UserData(0, "", "","","","", 0,0,null)) }
    //var user = remember { mutableStateOf(UserData(0, "Polina@gmail", "Polina","Bor","1234","Russian", 1,2,null))}
    proffViewModel.getUser(currentUserId, {usr -> user.value=usr})
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var imageUri = remember { mutableStateOf<Uri?>(null) }
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        imageUri.value = uri
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(bottom = down)) {
        Column(modifier = Modifier
            .background(PurpleApp)
            .padding(top = up, start = 25.dp, end = 25.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("main_page") }) {
            Spacer(modifier = Modifier.height(7.dp))
            val b: Bitmap? = byteArrayToBitmap(user.value.image_uri)
            AsyncImage(model = /*user.value.image_uri*/ b ?: R.mipmap.example_icon_user, contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape))
            Text(text = "Your profile, ${user.value.name}", color=Color.White, fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 9.dp))
        }
        Column(modifier = Modifier.padding(horizontal = 25.dp)){
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
                if(imageUri.value!=null){
                    Text("Фото загружается, подождите немного", modifier = Modifier.align(Alignment.Center))
                }
            }
            Button(onClick = {
                onThemeChange()
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                var text: String
                if(sharedPrefs.getBoolean("isDarkTheme", false)){
                    text = "Switch to Light"
                }else{
                    text = "Switch to Dark"
                }
                Text(text=text, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
            }
            Button(onClick = { navController.navigate("language_select")
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Change mother language", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
            }
            if(imageUri.value!=null){
                LaunchedEffect(Unit) {
                    val bitmapNew = uriToBitmap(imageUri.value!!, context)
                    val tempFile = File(context.getExternalFilesDir(null)!!, "templeIcon.png")
                    val fileOut = FileOutputStream(tempFile)
                    bitmapNew!!.compress(Bitmap.CompressFormat.PNG, 100, fileOut)
                    fileOut.close()
                    navController.navigate("resize_image_screen")
                    imageUri.value=null
                }
            }
            Button(onClick = {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Change your image", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
            }
            Button(onClick = {
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Logout", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp), color = Color.White)
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun ResizeImage(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var currentUserId = sharedPrefs.getInt("userId", -2)
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val filePath = "$path/templeIcon.png"
    var image=BitmapFactory.decodeFile(filePath)
    File(filePath).deleteOnExit()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(bottom = down)) {
        Box(modifier = Modifier.background(PurpleApp).fillMaxWidth().padding(15.dp).clickable { navController.navigate("profile_screen") }) {
            Column{
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Your photo is gorgeous!",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }



    }
}







fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}


fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
    return if(byteArray==null){
        null
    }else{
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}


@RequiresApi(Build.VERSION_CODES.P)
fun uriToBitmap(uri: Uri?, context: Context): Bitmap? {
    return try {
        val source = ImageDecoder.createSource(context.contentResolver, uri!!)
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}