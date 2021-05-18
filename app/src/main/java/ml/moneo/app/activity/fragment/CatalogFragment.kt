package ml.moneo.app.activity.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class CatalogFragment(@LayoutRes res: Int) : Fragment(res) {

    open fun onCloseClick() {}
    open fun onStartClick() {}
}