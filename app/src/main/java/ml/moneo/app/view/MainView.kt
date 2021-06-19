package ml.moneo.app.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.view.component.ContextPopup
import ml.moneo.app.view.component.CoolCamera
import ml.moneo.app.view.component.QRSwitch
import java.util.*

@Composable
fun WelcomeView() {
    var possibleIds by remember { mutableStateOf(mutableListOf<String>()) }
    var useQR by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var catalogOpen = false

    CoolCamera(useQR, { result ->
        if (result.isEmpty()) {
            return@CoolCamera
        }

        val list = mutableListOf<String>()
        result.forEach {
            val identification = it.text

            if (identification.equals("background", true)) {
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
                .statusBarsHeight(72.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.background,
                            Color.Transparent
                        )
                    )
                )
        ) {
            Row(Modifier.padding(16.dp).statusBarsPadding()) {
                QRSwitch(useQR) { useQR = it }
            }
        }
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
                )
                .navigationBarsPadding(),
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
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)) {
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
