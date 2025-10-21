package com.tomli.profftask

import android.app.Application
import com.tomli.profftask.databases.ProffDB

class Applic: Application() {
    val database by lazy{ ProffDB.createDB(this) }

}