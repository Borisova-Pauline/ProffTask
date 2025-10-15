package com.tomli.profftask.databases

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomli.profftask.Applic
import kotlinx.coroutines.launch

class ProffViewModel(val database: ProffDB)  : ViewModel() {
    val threeBestUsers = database.dao.getThreeBestCount()
    val defaultUser = UserData(0, "", "","","","", 0,0, null)

    fun addNewUser(email: String,name: String, last_name: String, password: String, language: String, context: Context)=viewModelScope.launch {
        database.dao.addUser(email,name, last_name, password, language)
        val user = database.dao.getUserOnLogin(email, password) ?: defaultUser
        val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("login", true)
        editor.putInt("userId", user.id!!)
        editor.apply()
    }

    fun getUserOnLogin(email: String, password: String, onReturn:(user: UserData, notHave: Boolean)-> Unit)=viewModelScope.launch {
        if(database.dao.getUserOnLoginIsHave(email, password)>0){
            val user = database.dao.getUserOnLogin(email, password)
            onReturn(user!!, false)
        }else{
            onReturn(defaultUser,
                true)
        }
    }

    fun getUser(id: Int, onReturn: (user: UserData) -> Unit)=viewModelScope.launch {
        var user = mutableStateOf(database.dao.getUser(id) ?: defaultUser)
        onReturn(user.value)
    }

    fun setNewRightChoiceCount(id: Int, count: Int)=viewModelScope.launch {
        if(count==0){
            database.dao.setNewRightChoiceCount(id, count)
        }else{
            val user = database.dao.getUser(id) ?: defaultUser
            database.dao.setNewRightChoiceCount(id, user.right_choice_count!!+1)
        }
    }

    fun updateIcon(id: Int, image: ByteArray)= viewModelScope.launch {
        database.dao.changeIcon(id, image)
    }


    companion object{
        val factory: ViewModelProvider.Factory= object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as Applic).database
                return ProffViewModel(database) as T
            }
        }
    }
}