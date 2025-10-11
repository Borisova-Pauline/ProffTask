package com.tomli.profftask.databases

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.tomli.profftask.Applic
import kotlinx.coroutines.launch

class ProffViewModel(val database: ProffDB)  : ViewModel() {

    fun addNewUser(email: String,name: String, last_name: String, password: String, language: String, context: Context)=viewModelScope.launch {
        database.dao.addUser(email,name, last_name, password, language)
        val user = database.dao.getUserOnLogin(email, password) ?: UserData(0, "", "","","","", 0,0)
        val sharedPrefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("login", true)
        editor.putInt("userId", user?.id!!)
        editor.apply()
    }

    fun getUserOnLogin(email: String, password: String, onReturn:(user: UserData, notHave: Boolean)-> Unit)=viewModelScope.launch {
        if(database.dao.getUserOnLoginIsHave(email, password)>0){
            val user = database.dao.getUserOnLogin(email, password)
            onReturn(user!!, false)
        }else{
            onReturn(UserData(0, "", "","","","", 0,0),
                true)
        }
    }

    fun getUser(id: Int, onReturn: (user: UserData) -> Unit)=viewModelScope.launch {
        var user = mutableStateOf(database.dao.getUser(id) ?: UserData(0, "", "","","","", 0,0))
        onReturn(user.value!!)
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