package ml.moneo.app.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.activity.CatalogsOverviewActivity
import ml.moneo.app.activity.ManualActivity
import ml.moneo.app.databinding.FragmentCatalogOverviewBinding
import ml.moneo.app.model.Guide
import ml.moneo.app.view.component.CatalogAdapter
import ml.moneo.app.viewmodel.GuidesViewModel
import ml.moneo.app.viewmodel.ProductsViewModel

class GuidesCatalogFragment : CatalogFragment(R.layout.fragment_catalog_overview) {

    private lateinit var binding: FragmentCatalogOverviewBinding
    private lateinit var guidesViewModel: GuidesViewModel
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    private val gridRowCount: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        guidesViewModel = ViewModelProvider(this).get(GuidesViewModel::class.java)
        productsViewModel = ViewModelProvider(requireActivity()).get(ProductsViewModel::class.java)
        binding = FragmentCatalogOverviewBinding.bind(view)

        productsViewModel.getSelectedProduct().value?.let { guidesViewModel.getGuides(it.remoteId) }

        setupGuidesRecyclerview()
        applyGuidesSetup()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            guidesViewModel.resetSelectedGuide()
        }
    }

    override fun onCloseClick() {
        super.onCloseClick()
        (activity as CatalogsOverviewActivity?)?.showProductsCatalog()
    }

    override fun onStartClick() {
        super.onStartClick()
        val intent = Intent(context, ManualActivity::class.java)
        intent.putExtra("MANUAL_ID", guidesViewModel.getSelectedGuide().value?.guideId)
        startActivity(intent)
    }

    private fun setupGuidesRecyclerview() {
        recyclerView = binding.catalogRecyclerview
        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, gridRowCount)
        recyclerView.layoutManager = gridLayoutManager
        catalogAdapter = CatalogAdapter()
        recyclerView.adapter = catalogAdapter

        catalogAdapter.onItemClick = { item ->
            if (item is Guide) {
                val guide: Guide = item
                guidesViewModel.setSelectedGuide(guide.guideId)
                onStartClick()
            }
        }
    }

    private fun applyGuidesSetup() {
        guidesViewModel.getAvailableGuides().observe(viewLifecycleOwner,{
            catalogAdapter.apply {
                items = it
            }
        })
    }
}