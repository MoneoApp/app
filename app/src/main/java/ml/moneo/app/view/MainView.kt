package ml.moneo.app.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.app.R
import ml.moneo.app.activity.HelpActivity
import ml.moneo.app.util.openActivity
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.view.component.CoolCamera
import ml.moneo.app.activity.CreditsActivity
import ml.moneo.app.activity.PreferenceActivity
import java.io.IOException
import java.util.ArrayList

@Composable
fun WelcomeView() {
    var possibleIds by remember { mutableStateOf(mutableListOf<String>()) }
    val context = LocalContext.current

    var catalogOpen = false

    CoolCamera({ result ->
        if (result.isEmpty()) {
            return@CoolCamera
        }

        val list = mutableListOf<String>()
        result.forEach {
            val identification = it.text

            if (identification == "Background") {
                return@CoolCamera
            }
            list.add(identification)
        }

        if (possibleIds.count() < list.count()) {
            possibleIds = list
        }
    }, {
        if (possibleIds.count() <= 0) {
            val list = mutableListOf<String>()
            list.add(it);
            possibleIds = list;
        }
    }) {
        Toast.makeText(context, R.string.camera_error, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsHeight(12.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.background,
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colors.background
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Scan your device",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .align(Alignment.Center)
            )

            Box(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)) {
                ContextPopup()
            }
        }
    }

    DisposableEffect(possibleIds) {
        if (possibleIds.size == 0 || catalogOpen) {
            return@DisposableEffect onDispose {}
        }

        val job = GlobalScope.launch {
            delay(500)

            val intent = Intent(context, CatalogsOverviewActivity::class.java).apply {
                putStringArrayListExtra("PRODUCT_IDS", possibleIds as ArrayList<String>)
            }

            catalogOpen = true
            startActivity(context, intent, null)

            GlobalScope.launch {
                delay(3000)
                possibleIds = mutableListOf()
                catalogOpen = false
            }
        }

        onDispose {
            job.cancel()
        }
    }
}

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
