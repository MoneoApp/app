package ml.moneo.app.activity.fragment

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.moneo.ManualByIdQuery
import ml.moneo.app.R
import ml.moneo.app.databinding.FragmentManualBinding
import ml.moneo.app.viewmodel.ManualViewModel
import ml.moneo.type.InteractionType
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ManualFragmentPrep : Fragment() {
    private lateinit var manualViewModel: ManualViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manualViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ManualViewModel::class.java)
        Log.d("Logge", "test");
    }
}
