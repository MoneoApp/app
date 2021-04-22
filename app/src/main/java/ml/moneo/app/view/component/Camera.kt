package ml.moneo.app.view.component

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun Camera(getCases: () -> Array<UseCase> = { arrayOf() }, onError: () -> Unit) {
    val owner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            val providerFuture = ProcessCameraProvider.getInstance(context)
            val view = PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            providerFuture.addListener({
                val provider = providerFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(view.surfaceProvider) }

                try {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        owner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        *getCases()
                    )
                } catch (e: Exception) {
                    Log.d("moneta.camera", "Camera lifecycle binding failed", e)
                    onError()
                }
            }, ContextCompat.getMainExecutor(context))

            view
        }
    )
}
