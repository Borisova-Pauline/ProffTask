package com.tomli.profftask.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomli.profftask.R
import com.tomli.profftask.databases.ProffViewModel
import com.tomli.profftask.databases.UserData
import com.tomli.profftask.ui.theme.BlueButtonColor
import com.tomli.profftask.ui.theme.FieldBackColorLight
import com.tomli.profftask.ui.theme.ProffTaskTheme
import com.tomli.profftask.ui.theme.PurpleApp


@Composable
fun SignUpAccount(navController: NavController){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val email = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)) {
        Box(modifier = Modifier.background(PurpleApp).fillMaxWidth().padding(15.dp)) {
            Column {
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Signup",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier.padding(top = up).size(20.dp)
                    .clickable { navController.navigate("language_select") })
        }
        Text(text="Create an Account", fontSize = 24.sp, modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
            textAlign = TextAlign.Center)
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp)){
            Text(text="First Name")
            OutlinedTextField(value = firstName.value, onValueChange = {newText -> firstName.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Your First Name", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ))
            Spacer(modifier = Modifier.height(30.dp))
            Text(text="Last Name")
            OutlinedTextField(value = lastName.value, onValueChange = {newText -> lastName.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Your Last Name", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ))
            Spacer(modifier = Modifier.height(30.dp))
            Text(text="Email Address")
            OutlinedTextField(value = email.value, onValueChange = {newText -> email.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Email", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ))

            Button(onClick = {if(firstName.value!="" && lastName.value!="" && email.value!=""){
                navController.navigate("signup_password/${firstName.value}/${lastName.value}/${email.value}")
            }else{
                Toast.makeText(context, "Остались незаполненные поля", Toast.LENGTH_LONG).show()
            }
                }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Continue", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Row(modifier = Modifier.align(Alignment.Center)){
                    Text(text="Already you member? ", color = Color(0xff65686f))
                    Text(text="Login", color = BlueButtonColor,
                        modifier = Modifier.clickable { navController.navigate("login") })
                }
            }
        }
    }
}


@Composable
fun SignUpPassword(navController: NavController,name: String, lastName: String, email: String, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val okayRules = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val language = sharedPrefs.getString("languageCurrent", Languages.Russian.name)
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)) {
        Box(modifier = Modifier.background(PurpleApp).fillMaxWidth().padding(15.dp)) {
            Column {
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Signup",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier.padding(top = up).size(20.dp)
                    .clickable { navController.navigateUp() })
        }
        Text(text="Choose a Password", fontSize = 24.sp, modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
            textAlign = TextAlign.Center)
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp)){
            Text(text="Password")
            val hidePassword = remember { mutableStateOf(true) }
            OutlinedTextField(value = password.value, onValueChange = {newText -> password.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { Image(painter = painterResource(R.drawable.eye_password), contentDescription = null, modifier = Modifier.padding(15.dp).size(20.dp).clickable { hidePassword.value=!hidePassword.value }) },
                placeholder = { Text(text = "**********", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ), visualTransformation = if(hidePassword.value) {PasswordVisualTransformation()} else {
                    VisualTransformation.None})
            Spacer(modifier = Modifier.height(30.dp))
            Text(text="Confirm Password")
            val hidePasswordConfirm = remember { mutableStateOf(true) }
            OutlinedTextField(value = confirmPassword.value, onValueChange = {newText -> confirmPassword.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { Image(painter = painterResource(R.drawable.eye_password), contentDescription = null, modifier = Modifier.padding(15.dp).size(20.dp).clickable { hidePasswordConfirm.value=!hidePasswordConfirm.value }) },
                placeholder = { Text(text = "**********", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ), visualTransformation = if(hidePasswordConfirm.value) {PasswordVisualTransformation()} else {
                    VisualTransformation.None})

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp)){
                Checkbox(checked = okayRules.value, onCheckedChange = {
                    okayRules.value=!okayRules.value
                }, colors = CheckboxDefaults.colors(checkedColor = BlueButtonColor, uncheckedColor = BlueButtonColor))
                Text(text="I have made myself acquainted with the Rules and accept all its provisions,", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Button(onClick = {if(password.value!="" && confirmPassword.value!="" &&password.value==confirmPassword.value&&okayRules.value==true){
                proffViewModel.addNewUser(email, name, lastName, password.value, language!!, context)
                navController.navigate("main_page")
            }else if(password.value==confirmPassword.value){
                Toast.makeText(context, "Подтверждение пароля не совпадает с паролем", Toast.LENGTH_LONG).show()
            }else if(okayRules.value==false){
                Toast.makeText(context, "Нет соглашения с правилами", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Остались незаполненные поля", Toast.LENGTH_LONG).show()
            }
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Signup", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Row(modifier = Modifier.align(Alignment.Center)){
                    Text(text="Already you member? ", color = Color(0xff65686f))
                    Text(text="Login", color = BlueButtonColor,
                        modifier = Modifier.clickable { navController.navigate("login") })
                }
            }
        }
    }
}


@Composable
fun LogIn(navController: NavController, proffViewModel: ProffViewModel = viewModel(factory = ProffViewModel.factory)){
    val up = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val down = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = down)) {
        Box(modifier = Modifier.background(PurpleApp).fillMaxWidth().padding(15.dp)) {
            Column{
                Spacer(modifier = Modifier.height(up))
                Text(
                    text = "Login",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
                modifier = Modifier.padding(top=up).size(20.dp).clickable { navController.navigate("language_select") })
        }
        Image(painter = painterResource(R.drawable.picture_login), contentDescription = null,
            modifier = Modifier.size(120.dp).padding(top=10.dp).align(Alignment.CenterHorizontally))
        Text(
            text = "For free, join now and start learning",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 20.dp),
            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp)){
            Text(text="Email Address")
            OutlinedTextField(value = email.value, onValueChange = {newText -> email.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Email", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ))
            Spacer(modifier = Modifier.height(30.dp))
            Text(text="Password")
            val hidePassword = remember { mutableStateOf(true) }
            OutlinedTextField(value = password.value, onValueChange = {newText -> password.value = newText},
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { Image(painter = painterResource(R.drawable.eye_password), contentDescription = null, modifier = Modifier.padding(15.dp).size(20.dp).clickable { hidePassword.value=!hidePassword.value }) },
                placeholder = { Text(text = "**********", color=Color(0xffabacb1)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleApp, unfocusedBorderColor = FieldBackColorLight,
                    cursorColor = PurpleApp,
                    unfocusedContainerColor = FieldBackColorLight, focusedContainerColor = FieldBackColorLight
                ), visualTransformation = if(hidePassword.value) {PasswordVisualTransformation()} else {
                    VisualTransformation.None})
            Text(text="Forgot Password", color = Color(0xffc1235d))
            Button(onClick = {
                if(email.value!=""&&password.value!=""){
                    var userThis = UserData(0, "", "","","","", 0,0)
                    var isHaveNot = mutableStateOf(false)
                    proffViewModel.getUserOnLogin(email.value, password.value, {user, notHave ->  userThis=user; isHaveNot.value=notHave})
                    Log.v("error", "after user serching $isHaveNot")
                    if(!isHaveNot.value){
                        Toast.makeText(context, "Ошибка в почте или пароле", Toast.LENGTH_LONG).show()
                    }else{
                        val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putBoolean("login", true)
                        editor.putInt("userId", userThis.id!!)
                        editor.apply()
                        navController.navigate("main_page")
                    }
                }else{
                    Toast.makeText(context, "Остались незаполненные поля", Toast.LENGTH_LONG).show()
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = BlueButtonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp), shape = RoundedCornerShape(15.dp)
            ) {
                Text(text="Login", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
            }
            Box(modifier = Modifier.fillMaxWidth()){
                Row(modifier = Modifier.align(Alignment.Center)){
                    Text(text="Not you member? ", color = Color(0xff65686f))
                    Text(text="Signup", color = BlueButtonColor,
                        modifier = Modifier.clickable { navController.navigate("signup_account") })
                }
            }

        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProffTaskTheme {
        //SignUpPassword(navController = rememberNavController())
    }
}