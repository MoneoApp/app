package ml.moneo.app.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.databinding.ActivityProductOverviewBinding
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.component.ProductsAdapter
import ml.moneo.app.viewmodel.GuidesViewModel
import ml.moneo.app.viewmodel.ProductsViewModel

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

        intent.getStringExtra("PRODUCT_NAME")?.let {
            productsViewModel.searchProducts(it)
        }
        productsViewModel.getSearchString().observe(this, {
            binding.productsInput.editText?.setText(it)
        })
        binding.productsInput.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO: Check wether products
            }
        })

        productsViewModel.getAllProductsBySearch().observe(this, { products ->
            setupProductsRecyclerview()
            Log.d("products", products.toString())
            productsAdapter.apply { items = products }
        })

//        binding.productCloseButton.setOnClickListener {
//            if (productsViewModel.getSelectedProduct().value != null) {
//                //applyProductsToView()
//                //allowGuideStart(false)
//            } else {
//                finish()
//            }
//        }
//
//        binding.productStartGuideButton.setOnClickListener {
//            openActivity(this, ManualActivity::class.java, false)
//        }

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
                    productsViewModel.setSelectedProduct(remote.remoteId)
                    items = guidesViewModel.getGuidesByRemoteId(remote.remoteId)!!
                }
            } else if (product is Guide) {
                val guide: Guide = product
                guidesViewModel.setSelectedGuide(guide.guideId)
            }
        }
    }
}