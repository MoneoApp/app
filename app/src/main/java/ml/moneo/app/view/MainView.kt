package ml.moneo.app.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.DeviceQuery
import ml.moneo.app.R
import ml.moneo.app.activity.ManualActivity
import ml.moneo.app.util.apolloClient
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.component.TFCamera
import java.io.IOException

@Composable
fun WelcomeView() {
    var open by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf<String?>(null) }
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
                    Button({ openActivity(context, ManualActivity::class.java) }) {
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
                    client.query(DeviceQuery(first)).await()
                } catch (e: ApolloException) {
                    return@launch
                }

                val device = response.data?.device
                if (device == null || response.hasErrors()) {
                    println(response.errors?.firstOrNull()?.message);

                    return@launch
                }

                Log.d("Loggie", "${device.brand} ${device.model}")

                label = "${device.model}"
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
            )
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
