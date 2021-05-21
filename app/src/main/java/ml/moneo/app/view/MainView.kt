package ml.moneo.app.view

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
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
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.DeviceByIdQuery
import ml.moneo.app.R
import ml.moneo.app.activity.HelpActivity
import ml.moneo.app.util.apolloClient
import ml.moneo.app.util.openActivity
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.view.component.TFCamera
import java.io.IOException

@Composable
fun WelcomeView() {
    var open by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf<String?>(null) }
    var id by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val labels = remember {
        try {
            val inputStream = context.assets.open("labels.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            String(buffer).split("\n", "\r\n")
        } catch (e: IOException) {
            e.printStackTrace()
            listOf()
        }
    }

    if (open) {
        Dialog({ open = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    Text(
                        text = label ?: "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Button({
                        open = false
                        val intent = Intent(context, CatalogsOverviewActivity::class.java).apply {
                            putExtra("PRODUCT_NAME", label)
                            putExtra("PRODUCT_ID", id)
                        }
                        startActivity(context, intent, null)
                    }) {
                        Text(stringResource(R.string.manual))
                    }
                }
            }
        }
    }

    TFCamera({ result ->
        if (open) {
            return@TFCamera
        } else if (result.isEmpty()) {
            label = null

            return@TFCamera
        }

        val first = labels[result.first().index]

        if (label != first) {

            val client = apolloClient()

            GlobalScope.launch {
                val response = try {
                    client.query(DeviceByIdQuery(first)).await()
                } catch (e: ApolloException) {
                    return@launch
                }

                val device = response.data?.device
                if (device == null || response.hasErrors()) {
                    println(response.errors?.firstOrNull()?.message);

                    return@launch
                }

                Log.d("Loggie", "${device.brand} ${device.model}")

                id = first
                label = device.model
            }
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

    DisposableEffect(label) {
        if (label == null || label == "Background") {
            return@DisposableEffect onDispose {}
        }

        val job = GlobalScope.launch {
            delay(500)
            open = true
        }

        onDispose {
            job.cancel()
        }
    }
}

sealed class ContextPopupItems(val icon: ImageVector, @StringRes val title: Int) {
    object Settings: ContextPopupItems(Icons.Filled.Settings, R.string.context_popup_settings);
    object Help: ContextPopupItems(Icons.Filled.Support, R.string.context_popup_help);
    object About: ContextPopupItems(Icons.Filled.Help, R.string.context_popup_about);
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
                openActivity(context, HelpActivity::class.java, true)
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
