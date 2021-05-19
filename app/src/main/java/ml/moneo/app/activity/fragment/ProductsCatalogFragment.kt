package ml.moneo.app.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.databinding.FragmentProductOverviewBinding
import ml.moneo.app.model.Remote
import ml.moneo.app.view.component.CatalogAdapter
import ml.moneo.app.viewmodel.ProductsViewModel

class ProductsCatalogFragment : CatalogFragment(R.layout.fragment_product_overview){

    private lateinit var binding: FragmentProductOverviewBinding
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    private val gridRowCount: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductOverviewBinding.bind(view)
        productsViewModel = ViewModelProvider(requireActivity()).get(ProductsViewModel::class.java)

        setupProductsRecyclerview()

        productsViewModel.getSearchString().observe(viewLifecycleOwner, {
            binding.catalogInput.editText?.setText(it)
        })

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