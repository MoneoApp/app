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
import ml.moneo.app.activity.ManualActivity
import ml.moneo.app.databinding.FragmentGuideOverviewBinding
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.component.CatalogAdapter
import ml.moneo.app.viewmodel.GuidesViewModel
import ml.moneo.app.viewmodel.ProductsViewModel

class GuidesCatalogFragment : Fragment(R.layout.fragment_guide_overview) {

    private lateinit var binding: FragmentGuideOverviewBinding
    private lateinit var guidesViewModel: GuidesViewModel
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    private val gridRowCount: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        guidesViewModel = ViewModelProvider(this).get(GuidesViewModel::class.java)
        productsViewModel = ViewModelProvider(requireActivity()).get(ProductsViewModel::class.java)
        binding = FragmentGuideOverviewBinding.bind(view)

        guidesViewModel.getSelectedGuide()?.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.startGuideButton.visibility = View.VISIBLE
            } else {
                binding.startGuideButton.visibility = View.INVISIBLE
            }
        })

        binding.guideCloseButton.setOnClickListener {
            (activity as CatalogsOverviewActivity?)?.showProductsCatalog()
        }

        binding.startGuideButton.setOnClickListener {
            openActivity(requireActivity(), ManualActivity::class.java, false)
        }

        setupGuidesRecyclerview()
        applyGuidesSetup()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            guidesViewModel.resetSelectedGuide()
            applyGuidesSetup()
        }
    }

    private fun setupGuidesRecyclerview() {
        recyclerView = binding.guidesRecyclerview
        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, gridRowCount)
        recyclerView.layoutManager = gridLayoutManager
        catalogAdapter = CatalogAdapter()
        recyclerView.adapter = catalogAdapter

        catalogAdapter.onItemClick = { item ->
            if (item is Guide) {
                val guide: Guide = item
                guidesViewModel.setSelectedGuide(guide.guideId)
            }
        }
    }

    private fun applyGuidesSetup() {
        catalogAdapter.apply {
            items = productsViewModel.getSelectedProduct().value?.let {
                guidesViewModel.getGuidesByRemoteId(
                    it.remoteId
                )
            }!!
        }
    }
}