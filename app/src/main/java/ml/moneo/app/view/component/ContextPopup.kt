package ml.moneo.app.view.component

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ml.moneo.app.R
import ml.moneo.app.activity.CreditsActivity
import ml.moneo.app.activity.HelpActivity
import ml.moneo.app.activity.PreferenceActivity
import ml.moneo.app.util.openActivity

sealed class ContextPopupItems(val icon: ImageVector, @StringRes val title: Int, val activity: Class<out Activity>) {
    object Settings: ContextPopupItems(Icons.Filled.Settings, R.string.context_popup_settings, PreferenceActivity::class.java);
    object Help: ContextPopupItems(Icons.Filled.Support, R.string.context_popup_help, HelpActivity::class.java);
    object About: ContextPopupItems(Icons.Filled.Help, R.string.context_popup_about, CreditsActivity::class.java);
}

@Composable
fun ContextPopup() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        ContextPopupItems.Settings,
        ContextPopupItems.Help,
        ContextPopupItems.About
    )

    IconButton(onClick = { expanded = true }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "Open context menu",
            tint = MaterialTheme.colors.onBackground
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    ) {
        items.forEachIndexed { _, s ->
            DropdownMenuItem(onClick = {
                openActivity(context, s.activity)
                expanded = false
            }) {
                Row {
                    Icon(
                        s.icon,
                        contentDescription = "Open ${stringResource(s.title)}",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(text = stringResource(s.title))
                }
            }
        }
    }
}