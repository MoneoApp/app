package ml.moneo.app.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.GuidesCatalogFragment
import ml.moneo.app.activity.fragment.ProductsCatalogFragment
import ml.moneo.app.databinding.ActivityCatalogsBinding
import ml.moneo.app.viewmodel.ProductsViewModel

class CatalogsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogsBinding
    private val productsViewModel: ProductsViewModel by viewModels()

    private val PRODUCTS_CATALOG_FRAGMENT = "products_catalog_fragment"
    private val GUIDES_CATALOG_FRAGMENT = "guides_catalog_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        binding = ActivityCatalogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProductsCatalog()
    }

    private fun showFragment(selectedFragment: String) {
        when (selectedFragment) {
            PRODUCTS_CATALOG_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT) != null) {
                    //if the fragment exists, show it.
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT)!!)
                        .commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.catalog_fragment_container,
                            ProductsCatalogFragment(),
                            PRODUCTS_CATALOG_FRAGMENT
                        )
                        .commit();
                }
                if (supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT) != null) {
                    //if the other fragment is visible, hide it.
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT)!!)
                        .commit();
                }
            }

            GUIDES_CATALOG_FRAGMENT -> {
                if (supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT) != null) {
                    //if the fragment exists, show it.
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(GUIDES_CATALOG_FRAGMENT)!!)
                        .commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.catalog_fragment_container,
                            GuidesCatalogFragment(),
                            GUIDES_CATALOG_FRAGMENT
                        )
                        .commit();
                }
                if (supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT) != null) {
                    //if the other fragment is visible, hide it.
                    supportFragmentManager.beginTransaction()
                        .hide(supportFragmentManager.findFragmentByTag(PRODUCTS_CATALOG_FRAGMENT)!!)
                        .commit();
                }
            }
        }
    }

    fun showProductsCatalog() {
        showFragment(PRODUCTS_CATALOG_FRAGMENT)
    }

    fun showGuidesCatalog() {
        showFragment(GUIDES_CATALOG_FRAGMENT)
    }
}