package ml.moneo.app.activity.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import ml.moneo.app.activity.CatalogsOverviewActivity

class CatalogFragment : Fragment(), CatalogsOverviewActivity.CloseClickListener {
    override fun onClick() {
        Log.d("products", "clickei")
    }
}