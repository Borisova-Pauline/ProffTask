package com.tomli.profftask.databases


import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: Dao) {
    fun getForWidget(id: Int): Flow<List<UserData>> = dao.getForWidget(id)
}