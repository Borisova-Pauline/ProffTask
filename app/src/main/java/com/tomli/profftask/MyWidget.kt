package com.tomli.profftask

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.tomli.profftask.ui.theme.MyFont


class MyWidget: GlanceAppWidget(errorUiLayout = R.layout.widget_error) {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            CompositionLocalProvider() {
                Content()
            }
        }
    }

    //override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    fun Content() {
        //Text(text="Hello", style = TextStyle(fontFamily = MyFont))
        Image(painter = painterResource(R.mipmap.example_icon_user), contentDescription = null)
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