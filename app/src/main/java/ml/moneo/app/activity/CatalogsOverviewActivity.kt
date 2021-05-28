package ml.moneo.app.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.CatalogFragment
import ml.moneo.app.activity.fragment.GuidesCatalogFragment
import ml.moneo.app.activity.fragment.ProductsCatalogFragment
import ml.moneo.app.databinding.ActivityCatalogsBinding
import ml.moneo.app.viewmodel.CatalogViewModel
import ml.moneo.app.viewmodel.GuidesViewModel

class CatalogsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogsBinding
    private lateinit var catalogViewModel: CatalogViewModel
    private lateinit var guidesViewModel: GuidesViewModel

    private val PRODUCTS_CATALOG_FRAGMENT = "products_catalog_fragment"
    private val GUIDES_CATALOG_FRAGMENT = "guides_catalog_fragment"
    private val LAST_VIEW_KEY = "lastView"

    private lateinit var currentFragmentTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityCatalogsBinding.inflate(layoutInflater)
        catalogViewModel = ViewModelProvider(this).get(CatalogViewModel::class.java)
        guidesViewModel = ViewModelProvider(this).get(GuidesViewModel::class.java)
        setContentView(binding.root)

        val lastView = savedInstanceState?.getInt(LAST_VIEW_KEY, 0)
        if (lastView != null) {
            if (lastView == 1) {
                showGuidesCatalog()
            }
        } else {
            showProductsCatalog()
        }

        binding.catalogCloseButton.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(currentFragmentTag) as CatalogFragment).onCloseClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val lastView = catalogViewModel.getCurrentView().value
        if (lastView != null) {
            outState.putInt(LAST_VIEW_KEY, lastView)
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
                    var fragment = ProductsCatalogFragment()
                    var bundle = Bundle()
                    bundle.putString("PRODUCT_ID", intent.getStringExtra("PRODUCT_ID"))

                    fragment.arguments = bundle

                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.catalog_fragment_container,
                            fragment,
                            PRODUCTS_CATALOG_FRAGMENT
                        )
                        .commit();


                }
                if (supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT) != null) {
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT)!!)
                        .commit();
                }

                currentFragmentTag = PRODUCTS_CATALOG_FRAGMENT
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

                currentFragmentTag = GUIDES_CATALOG_FRAGMENT
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

    fun blockUserInput(value: Boolean) {
        if (value) {
            binding.catalogProgressBar.visibility = View.VISIBLE
        } else if (!value) {
            binding.catalogProgressBar.visibility = View.INVISIBLE
        }
    }
}