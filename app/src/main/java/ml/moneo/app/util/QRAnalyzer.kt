package ml.moneo.app.util

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QRAnalyzer(
    private val onSuccess: (String) -> Unit,
    private val onError: () -> Unit
) : ImageAnalysis.Analyzer {
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val scanner = BarcodeScanning.getClient(options)

    @ExperimentalGetImage
    override fun analyze(proxy: ImageProxy) {
        val mediaImage = proxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { onSuccess(it.rawValue ?: "") }
            }
            .addOnFailureListener { onError() }
            .addOnCompleteListener { proxy.close() }
    }
}
