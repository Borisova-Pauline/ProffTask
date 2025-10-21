package com.tomli.profftask

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.tomli.profftask.ui.theme.PurpleApp


class MyWidget: GlanceAppWidget(errorUiLayout = R.layout.widget_error) {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        provideContent {
            CompositionLocalProvider() {
                Content()
            }
        }
    }

    @Composable
    fun Content() {
        Column (modifier = GlanceModifier.fillMaxSize().background(PurpleApp)){
            Text(text ="Hello", style = TextStyle(color = ColorProvider(Color.White)))
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