package ml.moneo.app.util

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions

class TFAnalyzer(
    private val onSuccess: (List<ImageLabel>) -> Unit,
    private val onError: () -> Unit
) : ImageAnalysis.Analyzer {
    private val model = LocalModel.Builder()
        .setAssetFilePath("model.tflite")
        .build()
    private val options = CustomImageLabelerOptions.Builder(model)
        .setMaxResultCount(1)
        .setConfidenceThreshold(0.75f)
        .build()
    private val imageLabeler = ImageLabeling.getClient(options)

    @ExperimentalGetImage
    override fun analyze(proxy: ImageProxy) {
        val mediaImage = proxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)

            imageLabeler
                .process(image)
                .addOnSuccessListener { onSuccess(it) }
                .addOnFailureListener { onError() }
                .addOnCompleteListener { proxy.close() }
        }
    }
}
