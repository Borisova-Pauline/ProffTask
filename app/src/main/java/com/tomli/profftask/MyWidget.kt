package com.tomli.profftask

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.tomli.profftask.databases.ProffDB
import com.tomli.profftask.databases.TaskRepository
import com.tomli.profftask.ui.theme.OrangeApp
import com.tomli.profftask.ui.theme.PurpleApp


class MyWidget: GlanceAppWidget(errorUiLayout = R.layout.widget_error) {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        var currentUserId = prefs.getInt("userId", -2)

        val repo = TaskRepository(ProffDB.createDB(context).dao)


        provideContent {
            CompositionLocalProvider() {
                val fourPeople = repo.getForWidget(currentUserId).collectAsState(initial = emptyList())

                Column (modifier = GlanceModifier.fillMaxSize().background(PurpleApp).padding(horizontal = 15.dp, vertical = 10.dp)){
                    if(currentUserId>-2){
                        Text(text ="You place is! Awesome", style = TextStyle(color = ColorProvider(Color.White)),
                            modifier = GlanceModifier.padding(bottom = 5.dp))
                        fourPeople.value.forEach{
                            var name: String
                            var colorBack: Color
                            if(currentUserId==it.id){
                                name = "You"
                                colorBack = OrangeApp
                            }else{
                                name = it.name!!
                                colorBack = Color.White
                            }

                            Row(modifier = GlanceModifier.fillMaxWidth().background(ColorProvider(colorBack)).padding(horizontal = 15.dp, vertical = 5.dp).cornerRadius(10.dp)) {
                                Text(text = name, style = TextStyle(color = ColorProvider(Color.Black)))
                                Spacer(modifier = GlanceModifier.defaultWeight())
                                Text(text = "${(it.guess_animal_count!!+it.right_choice_count!!)} points", style = TextStyle(color = ColorProvider(Color.Black)))
                            }
                            Spacer(modifier = GlanceModifier.height(7.dp))
                        }
                    }else{
                        Text(text ="You should log in application", style = TextStyle(color = ColorProvider(Color.White)))
                    }
                }
            }
        }
    }


    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        Log.v("MyAppWidget", "Composition failed: ", throwable)
        val customErrorView = RemoteViews(context.packageName, R.layout.widget_error)
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, customErrorView)
    }

}