package ml.moneo.app.activity.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.TransformableNode
import ml.moneo.ManualByIdQuery
import ml.moneo.app.R
import ml.moneo.app.databinding.FragmentManualBinding
import ml.moneo.app.viewmodel.ManualViewModel
import java.util.*

class ManualFragment : Fragment(), Scene.OnUpdateListener {
    private lateinit var manualViewModel: ManualViewModel
    private lateinit var binding: FragmentManualBinding
    private lateinit var fragment: ARFragment

    private var active: Boolean = false
    private var anchor: Anchor? = null
    private var currentNode: AnchorNode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentManualBinding.inflate(inflater, container, false)
        manualViewModel = ViewModelProvider(this).get(ManualViewModel::class.java)

        return binding.root
    }

    fun initializeAR(manualId: String)
    {
        manualViewModel.setupManual(manualId)

        manualViewModel.getManual().observe(viewLifecycleOwner, {
            if(it != null)
            {
                manualViewModel.getCurrentStep().observe(viewLifecycleOwner, {
                    binding.includedSteps.manualTextview.text =
                        getString(R.string.manual_step, it + 1, manualViewModel.getDescription())
                })

                binding.includedSteps.nextButton.setOnClickListener {
                    manualViewModel.next()

                    if(active)
                    {
                        showLayout(this.anchor!!)
                    }
                }

                binding.includedSteps.previousButton.setOnClickListener {
                    manualViewModel.previous()

                    if(active)
                    {
                        showLayout(this.anchor!!)
                    }
                }

                binding.includedSteps.stepsCloseButton.setOnClickListener{
                    activity?.finish()
                }

                fragment = this.childFragmentManager.findFragmentById(R.id.camera_view) as ARFragment
                fragment.arSceneView.scene.addOnUpdateListener(this)

                fragment.arSceneView.planeRenderer.isEnabled = false;
            }
        })
    }

    fun setupDatabase(config: Config, session: Session) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.white_remote_square)
        val db = AugmentedImageDatabase(session)

        db.addImage("image", bitmap)
        config.augmentedImageDatabase = db
    }

    override fun onUpdate(frameTime: FrameTime?) {
        val frame = fragment.arSceneView.arFrame
        val images = frame?.getUpdatedTrackables(AugmentedImage::class.java)

        images?.filter { it.trackingState == TrackingState.TRACKING }?.forEach { image ->
            this.anchor = image.createAnchor(image.centerPose)

            if (!active) {
                active = true
                showLayout(this.anchor!!)
            }
        }
    }

    private fun showLayout(anchor: Anchor) {
        if(currentNode != null)
        {
            fragment.arSceneView.scene.removeChild(currentNode)
        }
        else
        {
            fragment.planeDiscoveryController.hide()
        }

        val anchorNode = AnchorNode(anchor)
        currentNode = anchorNode

        var anchorPosition: ManualByIdQuery.Interaction? = manualViewModel.getAnchorPosition()
        var anchorCenter = Vector3(
            ((anchorPosition!!.x.toFloat() + (anchorPosition!!.width / 2)).toFloat()),
            0.0f,
            ((anchorPosition.y.toFloat() + (anchorPosition!!.height / 2)).toFloat())
        )

        manualViewModel.getInteractions().forEach { interaction ->
            ViewRenderable.builder()
                .setView(fragment.context, R.layout.button)
                .build()
                .thenAccept { renderable ->
                    renderable.isShadowCaster = false
                    renderable.isShadowReceiver = false

                    val node = TransformableNode(this.fragment.transformationSystem)

                    node.renderable = renderable
                    node.localRotation = Quaternion.axisAngle(Vector3(1f, 0f, 0f), 90f)

                    var nodePosition = Vector3((interaction.x.toFloat() - anchorCenter.x), 0.0f, (interaction.y.toFloat() - anchorCenter.z));

                    node.localPosition = Vector3(nodePosition.x/9000, nodePosition.y, nodePosition.z/9000)
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
