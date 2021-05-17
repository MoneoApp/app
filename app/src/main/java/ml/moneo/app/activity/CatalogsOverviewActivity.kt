package ml.moneo.app.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.GuidesCatalogFragment
import ml.moneo.app.activity.fragment.ProductsCatalogFragment
import ml.moneo.app.databinding.ActivityCatalogsBinding
import ml.moneo.app.viewmodel.CatalogViewModel
import ml.moneo.app.viewmodel.ProductsViewModel

class CatalogsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogsBinding

    private lateinit var catalogViewModel: CatalogViewModel

    private val PRODUCTS_CATALOG_FRAGMENT = "products_catalog_fragment"
    private val GUIDES_CATALOG_FRAGMENT = "guides_catalog_fragment"

    //private lateinit var closeClickListener: CloseClickListener
    private lateinit var productsFrag: CloseClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityCatalogsBinding.inflate(layoutInflater)
        catalogViewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)
        setContentView(binding.root)

        val lastView = savedInstanceState?.getInt("lastView", 0)
        Log.d("catalog", "creation lastview: " + lastView)

        if (lastView != null) {
            if (lastView == 1) {
                showGuidesCatalog()
            }
        } else {
            showProductsCatalog()
        }

        catalogViewModel.getCurrentView().observe(this, {
            //Log.d("catalog", "current view $it")
        })


        //CloseClickListener = CloseClickListener()
        binding.catalogCloseButton.setOnClickListener { showProductsCatalog() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val lastView = catalogViewModel.getCurrentView().value
        Log.d("catalog", "saved lastview: $lastView")
        if (lastView != null) {
            outState.putInt("lastView", lastView)
        }
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(selectedFragment: String) {
        when (selectedFragment) {
            PRODUCTS_CATALOG_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT) != null) {
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT)!!)
                        .commit();
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.catalog_fragment_container,
                            ProductsCatalogFragment(),
                            PRODUCTS_CATALOG_FRAGMENT
                        )
                        .commit();
                }
                if (supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT) != null) {
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT)!!)
                        .commit();
                }
            }

            GUIDES_CATALOG_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT) != null) {
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT)!!)
                        .commit();
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.catalog_fragment_container,
                            GuidesCatalogFragment(),
                            GUIDES_CATALOG_FRAGMENT
                        )
                        .commit();
                }
                if (supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT) != null) {
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT)!!)
                        .commit();
                }
            }
        }
    }

    fun showProductsCatalog() {
        showFragment(PRODUCTS_CATALOG_FRAGMENT)
        catalogViewModel.setCurrentView(CatalogViewModel.CATALOGTYPES.PRODUCTS)
    }

    fun showGuidesCatalog() {
        showFragment(GUIDES_CATALOG_FRAGMENT)
        catalogViewModel.setCurrentView(CatalogViewModel.CATALOGTYPES.GUIDES)
    }

    interface CloseClickListener {
        fun onClick();
    }
}