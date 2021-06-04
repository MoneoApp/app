package ml.moneo.app.util

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.CustomRemoteModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.linkfirebase.FirebaseModelSource
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions

class TFAnalyzer(
    private val onSuccess: (List<ImageLabel>) -> Unit,
    private val onError: () -> Unit
) : ImageAnalysis.Analyzer {
    private var labeler: ImageLabeler? = null

    init {
        val model = CustomRemoteModel.Builder(FirebaseModelSource.Builder("moneo").build()).build()
        val downloadConditions = DownloadConditions.Builder().build()

        RemoteModelManager.getInstance().deleteDownloadedModel(model).addOnSuccessListener {
            RemoteModelManager.getInstance().download(model, downloadConditions).addOnSuccessListener {
                val options = CustomImageLabelerOptions.Builder(model)
                    .setMaxResultCount(99)
                    .setConfidenceThreshold(0.1f)
                    .build()

                labeler = ImageLabeling.getClient(options)
            }
        }
    }

    @ExperimentalGetImage
    override fun analyze(proxy: ImageProxy) {
        val mediaImage = proxy.image

        if (mediaImage != null && labeler != null) {
            val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)

            labeler!!
                .process(image)
                .addOnSuccessListener { onSuccess(it) }
                .addOnFailureListener { onError() }
                .addOnCompleteListener { proxy.close() }
        } else {
            proxy.close()
        }
    }
}
