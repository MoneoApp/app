package ml.moneo.app.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.databinding.FragmentCatalogOverviewBinding
import ml.moneo.app.model.Remote
import ml.moneo.app.view.component.CatalogAdapter
import ml.moneo.app.viewmodel.ProductsViewModel

class ProductsCatalogFragment : CatalogFragment(R.layout.fragment_catalog_overview) {

    private lateinit var binding: FragmentCatalogOverviewBinding
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    private val gridRowCount: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCatalogOverviewBinding.bind(view)
        productsViewModel = ViewModelProvider(requireActivity()).get(ProductsViewModel::class.java)

        arguments?.let {
            it.getStringArrayList("PRODUCT_IDS")?.let { it1 ->

                Log.d("labeltest", "product catalog fragment")
                Log.d("labeltest", it1.toString())

                productsViewModel.getProducts(
                    it1
                )
            }
        }

        Log.d("labeltest", productsViewModel.getAllProductsBySearch().value.toString())

        setupProductsRecyclerview()

        productsViewModel.getAllProductsBySearch().observe(requireActivity(), { products ->
            catalogAdapter.apply { items = products }
        })
    }

    override fun onCloseClick() {
        super.onCloseClick()
        activity?.finish()
    }

    private fun setupProductsRecyclerview() {
        recyclerView = binding.catalogRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, gridRowCount)
        recyclerView.layoutManager = gridLayoutManager
        catalogAdapter = CatalogAdapter()
        recyclerView.adapter = catalogAdapter

        catalogAdapter.onItemClick = { product ->
            if (product is Remote) {
                val remote: Remote = product
                catalogAdapter.apply {
                    productsViewModel.setSelectedProduct(remote.remoteId)
                    (activity as CatalogsOverviewActivity?)?.showGuidesCatalog()
                }
            }
        }
    }
}