package ml.moneo.app.view.component

import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.google.mlkit.vision.label.ImageLabel
import ml.moneo.app.util.TFAnalyzer
import java.util.concurrent.Executors

@Composable
fun TFCamera(onSuccess: (List<ImageLabel>) -> Unit, onError: () -> Unit) {
    val executor = remember { Executors.newSingleThreadExecutor() }

    Camera(
        getCases = {
            arrayOf(ImageAnalysis.Builder()
                .build()
                .also { it.setAnalyzer(executor, TFAnalyzer(onSuccess, onError)) })
        },
        onError = onError
    )

    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown()
        }
    }
}
