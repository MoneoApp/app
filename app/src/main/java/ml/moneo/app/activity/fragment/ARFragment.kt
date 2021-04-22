package ml.moneo.app.activity.fragment

import com.google.ar.core.Config
import com.google.ar.core.Session
import ml.moneo.app.R
import com.google.ar.sceneform.ux.ArFragment as Fragment

class ARFragment : Fragment() {
    override fun getSessionConfiguration(session: Session?): Config {
        val config = Config(session)

        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        config.focusMode = Config.FocusMode.AUTO
        session!!.configure(config)

        this.arSceneView.setupSession(session)

        val manualFragment: ManualFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.included_manual) as ManualFragment
        manualFragment.setupDatabase(config, session)

        return config
    }
}
