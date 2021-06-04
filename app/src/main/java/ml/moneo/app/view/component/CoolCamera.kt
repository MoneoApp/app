package ml.moneo.app.view.component

import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.google.mlkit.vision.label.ImageLabel
import ml.moneo.app.util.TFAnalyzer
import java.util.concurrent.Executors

@Composable
fun CoolCamera(
    onSuccess: (List<ImageLabel>) -> Unit,
    onJump: (data: String) -> Unit,
    onError: () -> Unit
) {
    val tfExecutor = remember { Executors.newSingleThreadExecutor() }
    val qrExecutor = remember { Executors.newSingleThreadExecutor() }

    Camera(
        getCases = {
            arrayOf(
                ImageAnalysis.Builder()
                    .build()
                    .also { it.setAnalyzer(tfExecutor, TFAnalyzer(onSuccess, onError)) },
//                ImageAnalysis.Builder()
//                    .build()
//                    .also { it.setAnalyzer(qrExecutor, QRAnalyzer(onJump, onError)) }
            )
        },
        onError = onError
    )

    DisposableEffect(Unit) {
        onDispose {
            tfExecutor.shutdown()
            qrExecutor.shutdown()
        }
    }
}
