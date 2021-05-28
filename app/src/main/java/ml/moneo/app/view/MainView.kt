package ml.moneo.app.view

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.DeviceByIdQuery
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.util.apolloClient
import ml.moneo.app.view.component.CoolCamera
import java.io.IOException

@Composable
fun WelcomeView() {
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

    CoolCamera({ result ->
        if (result.isEmpty()) {
            id = null

            return@CoolCamera
        }

        val first = labels[result.first().index]

        if (id == first) {
            return@CoolCamera
        }

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

            Log.d("MON/API", "${device.brand} ${device.model}")

            id = first
        }
    }, { id = it }) {
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

    DisposableEffect(id) {
        if (id == null) {
            return@DisposableEffect onDispose {}
        }

        val job = GlobalScope.launch {
            delay(500)

            val intent = Intent(context, CatalogsOverviewActivity::class.java).apply {
                putExtra("PRODUCT_ID", id)
            }

            startActivity(context, intent, null)
        }

        onDispose {
            job.cancel()
        }
    }
}
