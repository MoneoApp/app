package ml.moneo.app.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.databinding.FragmentProductOverviewBinding
import ml.moneo.app.model.Remote
import ml.moneo.app.view.component.CatalogAdapter
import ml.moneo.app.viewmodel.ProductsViewModel

class ProductsCatalogFragment : Fragment(R.layout.fragment_product_overview),
    CatalogsOverviewActivity.CloseClickListener {

    private lateinit var binding: FragmentProductOverviewBinding
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    private val gridRowCount: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductOverviewBinding.bind(view)
        productsViewModel = ViewModelProvider(requireActivity()).get(ProductsViewModel::class.java)

        //TODO: Get search string from previous activity
//        intent.getStringExtra("PRODUCT_NAME")?.let {
//            productsViewModel.searchProducts(it)
//        }

        productsViewModel.getSearchString().observe(viewLifecycleOwner, {
            binding.catalogInput.editText?.setText(it)
        })

        productsViewModel.getAllProductsBySearch().observe(requireActivity(), { products ->
            //setupProductsRecyclerview()
            catalogAdapter.apply { items = products }
        })

        setupProductsRecyclerview()

//        binding.productCloseButton.setOnClickListener {
//            activity?.finish()
//        }
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

    override fun onClick() {
        Log.d("products", "click!")
    }
}