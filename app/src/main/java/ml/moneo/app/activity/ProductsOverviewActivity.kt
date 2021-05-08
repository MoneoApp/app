package ml.moneo.app.activity

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.databinding.ActivityProductOverviewBinding
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote
import ml.moneo.app.view.component.ProductsAdapter
import ml.moneo.app.viewmodel.GuidesViewModel
import ml.moneo.app.viewmodel.ProductsViewModel
import kotlin.reflect.typeOf

class ProductsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductOverviewBinding
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var guidesViewModel: GuidesViewModel

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    private val gridRowCount: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        guidesViewModel = ViewModelProvider(this).get(GuidesViewModel::class.java)
        binding = ActivityProductOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("PRODUCT_NAME")?.let { productsViewModel.setProductSearchString(it) }
        //binding.productsInput.editText?.setText(productsViewModel.getProductSearchString().value)

        guidesViewModel.getSelectedGuide().observe(this, { guide ->
            if (guide != null) {
                binding.productStartGuideButton.visibility = View.VISIBLE
            } else {
                binding.productStartGuideButton.visibility = View.INVISIBLE
            }
        })

        productsViewModel.getAllProducts().observe(this, { products ->
            setupProductsRecyclerview()
            productsAdapter.apply { items = products }
        })

        binding.productCloseButton.setOnClickListener {
            //TODO: Check if we are viewing remotes or guides
            if (productsViewModel.getSelectedProduct().value != null) {
                applyProductsToView()
            } else {
                finish()
            }
        }

        binding.productStartGuideButton.setOnClickListener {


        }

        //Apply new items to adapter
        //Change onclicks?

        //TODO: Set search label based on extraString
    }

    private fun setupProductsRecyclerview() {
        productsRecyclerView = binding.productsRecyclerview
        val gridLayoutManager = GridLayoutManager(this, gridRowCount)
        productsRecyclerView.layoutManager = gridLayoutManager
        productsAdapter = ProductsAdapter()
        productsRecyclerView.adapter = productsAdapter

        productsAdapter.onItemClick = { product ->
            if (product is Remote) {
                val remote: Remote = product
                productsAdapter.apply {
                    items = guidesViewModel.getGuidesByRemoteId(remote.remoteId)!!
                    productsViewModel.setSelectedProduct(remote.remoteId)
                }
            } else if (product is Guide) {
                val guide: Guide = product
                guidesViewModel.setSelectedGuide(guide.guideId)
            }
        }
    }

    private fun applyProductsToView() {
        Log.d("products", "applied items before: " + productsAdapter.items)
        productsAdapter.apply { items = productsViewModel.getAllProducts().value!! }
        Log.d("products", "applied items after: " + productsAdapter.items)
    }
}