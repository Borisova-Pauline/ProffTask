package com.tomli.profftask.databases

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import com.tomli.profftask.Applic

class TaskRepository(private val dao: Dao) {
    suspend fun getForWidget(id: Int): List<UserData> = dao.getForWidget(id)
}


/*class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Получаем данные из базы
            val repository = (applicationContext as Applic).taskRepository
            val pendingCount = repository.getPendingTasksCount()
            val topTasks = repository.getTopPendingTasks()

            // Сохраняем данные в SharedPreferences для виджета
            val prefs = applicationContext.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            prefs.edit().apply {
                putInt("pending_count", pendingCount)
                putString("top_tasks", Gson().toJson(topTasks))
            }.apply()

            // Обновляем виджет
            updateWidget(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun updateWidget(context: Context) {
        val glanceId = GlanceAppWidgetManager(context)
            .getGlanceIds(MyWidget::class.java)
            .firstOrNull()

        glanceId?.let {
            MyWidget().update(context, it)
        }
    }
}*/