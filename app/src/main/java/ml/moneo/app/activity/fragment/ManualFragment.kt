package ml.moneo.app.activity.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.ar.core.*
import com.google.ar.core.exceptions.ImageInsufficientQualityException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.TransformableNode
import ml.moneo.ManualByIdQuery
import ml.moneo.app.R
import ml.moneo.app.databinding.FragmentManualBinding
import ml.moneo.app.viewmodel.ManualViewModel
import ml.moneo.type.InteractionType
import java.util.*
import android.widget.Toast


class ManualFragment : Fragment(), Scene.OnUpdateListener {
    private lateinit var manualViewModel: ManualViewModel
    private lateinit var binding: FragmentManualBinding
    private lateinit var fragment: ARFragment

    private var active: Boolean = false
    private var anchor: Anchor? = null
    private var currentNode: AnchorNode? = null
    private var image: AugmentedImage? = null

    private var manualId: String? = null;
    private val scaleFactor = 45;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentManualBinding.inflate(inflater, container, false)
        manualViewModel =
            ViewModelProvider(activity as ViewModelStoreOwner).get(ManualViewModel::class.java)


        binding.includedStepsPrelaunch.stepsCloseButton.setOnClickListener {
            activity?.finish()
        }

        swapStepView(false)
        setupSteps()

        return binding.root
    }

    private fun swapStepView(showSteps: Boolean) {
        if (showSteps) {
            binding.includedSteps.root.visibility = View.VISIBLE
            binding.includedStepsPrelaunch.root.visibility = View.GONE
        } else {
            binding.includedSteps.root.visibility = View.GONE
            binding.includedStepsPrelaunch.root.visibility = View.VISIBLE
        }
    }

    private fun setupSteps() {
        if (manualViewModel.getManual().value != null) {

            binding.includedSteps.previousButton.isEnabled = false

            if (manualViewModel.getManual().value != null) {

                manualViewModel.getCurrentStep().observe(viewLifecycleOwner, {
                    binding.includedSteps.manualTextview.text =
                        getString(R.string.manual_step, it + 1, manualViewModel.getDescription())
                })

                binding.includedSteps.nextButton.setOnClickListener {
                    manualViewModel.next()

                    if (manualViewModel.currentStep() >= (manualViewModel.maxSteps() - 1)) {
                        it.isEnabled = false
                    }
                    binding.includedSteps.previousButton.isEnabled = true


                    if (active) {
                        showLayout(this.anchor!!, this.image!!)
                    }
                }

                binding.includedSteps.previousButton.setOnClickListener {
                    manualViewModel.previous()

                    if (manualViewModel.currentStep() <= (manualViewModel.minSteps())) {
                        it.isEnabled = false
                    }
                    binding.includedSteps.nextButton.isEnabled = true

                    if (active) {
                        showLayout(this.anchor!!, this.image!!)
                    }
                }

                binding.includedSteps.stepsCloseButton.setOnClickListener {
                    activity?.finish()
                }

                fragment =
                    this.childFragmentManager.findFragmentById(R.id.camera_view) as ARFragment
                fragment.arSceneView.scene.addOnUpdateListener(this)

                fragment.arSceneView.planeRenderer.isEnabled = false;
            }
        }
    }

    fun setupDatabase(config: Config, session: Session) {
        val db = AugmentedImageDatabase(session)
        try {
            db.addImage("image", manualViewModel.getBitmap().value)
        } catch (e: ImageInsufficientQualityException) {
            val errorToast = Toast.makeText(
                this.context,
                "AR Insufficient anchor quality",
                Toast.LENGTH_SHORT
            )
            errorToast.show()
        }
        config.augmentedImageDatabase = db
    }

    override fun onUpdate(frameTime: FrameTime?) {
        val frame = fragment.arSceneView.arFrame
        val images = frame?.getUpdatedTrackables(AugmentedImage::class.java)

        images?.filter { it.trackingState == TrackingState.TRACKING }?.forEach { image ->
            this.anchor = image.createAnchor(image.centerPose)
            this.image = image

            if (!active) {
                active = true
                showLayout(this.anchor!!, image)
                swapStepView(true)
            }
        }
    }

    private fun showLayout(anchor: Anchor, aImage: AugmentedImage) {
        if (currentNode != null) {
            fragment.arSceneView.scene.removeChild(currentNode)
        } else {
            fragment.planeDiscoveryController.hide()
        }

        val anchorNode = AnchorNode(anchor)
        currentNode = anchorNode

        var anchorData: ManualByIdQuery.Interaction? = manualViewModel.getAnchorData()

        manualViewModel.getInteractions().forEach { interaction ->
            var future = ViewRenderable.builder();

            if (interaction.type == InteractionType.SQUARE) {
                future.setView(fragment.context, R.layout.button_square);
            } else if (interaction.type == InteractionType.CIRCLE) {
                future.setView(fragment.context, R.layout.button_circle);
            }

            future
                .build()
                .thenAccept { renderable ->
                    renderable.isShadowCaster = false
                    renderable.isShadowReceiver = false
                    var element =
                        renderable.view.findViewById<ImageView>(R.id.overlay_button) as ImageView

                    val node = TransformableNode(this.fragment.transformationSystem)

                    element.layoutParams.height =
                        (interaction.height / anchorData!!.height * scaleFactor).toInt()
                    element.layoutParams.width =
                        (interaction.width / anchorData!!.width * scaleFactor).toInt()
                    ImageViewCompat.setImageTintList(element,
                        interaction.color?.let {
                            ColorStateList.valueOf(Color.parseColor(it))
                        })

                    node.renderable = renderable
                    node.localRotation = Quaternion.multiply(
                        Quaternion.axisAngle(Vector3(1f, 0f, 0f), 90f),
                        Quaternion.axisAngle(Vector3(0f, 0f, 1f), interaction.rotation.toFloat())
                    );

                    var xPos =
                        (((interaction.x.toFloat() + (interaction.width.toFloat() / 2)) - anchorData!!.x.toFloat()) / ((anchorData!!.width.toFloat() + anchorData!!.x.toFloat()) - anchorData!!.x.toFloat())) - .5f
                    var yPos =
                        (((interaction.y.toFloat()) - anchorData!!.y.toFloat()) / ((anchorData!!.height.toFloat() + anchorData!!.y.toFloat()) - anchorData!!.y.toFloat())) - .5f
                    var pos = Vector3(
                        (((xPos)) * aImage.extentX),
                        0.0f,
                        (((yPos)) * aImage.extentZ)
                    )

                    node.localPosition = Vector3(pos)

                    //node.localScale = Vector3(interaction.width.toFloat(), 1.0f, interaction.height.toFloat())
                    node.setParent(anchorNode)
                }
                .exceptionally { error ->
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setMessage(error.message)
                            .setTitle("Error")
                            .create()
                            .show()
                    }

                    return@exceptionally null
                }
        }

        this.fragment.arSceneView.scene.addChild(anchorNode)
    }
}
