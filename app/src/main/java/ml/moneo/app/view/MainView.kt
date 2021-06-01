package ml.moneo.app.view

import android.content.Intent
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
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.component.CoolCamera
import java.io.IOException
import java.util.ArrayList

@Composable
fun WelcomeView() {
    var possibleIds by remember { mutableStateOf(mutableListOf<String>()) }
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

    var catalogOpen = false

    CoolCamera({ result ->
        if (result.isEmpty()) {
            return@CoolCamera
        }

        val list = mutableListOf<String>()
        result.forEach {
            val identification = labels[it.index]

            if (identification == "Background") {
                return@CoolCamera
            }
            list.add(identification)
        }

        if (possibleIds.count() < list.count()) {
            possibleIds = list
        }
    }, { }) {
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