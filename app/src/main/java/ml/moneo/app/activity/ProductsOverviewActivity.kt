package ml.moneo.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.databinding.ActivityProductOverviewBinding
import ml.moneo.app.model.Remote
import ml.moneo.app.view.component.ProductsAdapter
import ml.moneo.app.viewmodel.ProductsViewModel

class ProductsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductOverviewBinding
    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    private val gridRowCount: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        binding = ActivityProductOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchString: String? = intent.getStringExtra("PRODUCT_NAME")
        binding.productsInput.editText?.setText(searchString)
        binding.productCloseButton.setOnClickListener {
            //TODO: Check if we are viewing remotes or guides
            finish()
        }

        productsViewModel.getAllProducts().observe(this, { products ->
            setupProductsRecyclerview()
            productsAdapter.apply { items = products }
        })

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
            val remote: Remote = product as Remote
            productsAdapter.apply {
                items =
                    productsViewModel.getGuidesByRemoteId(remote.remoteId)!!
            }
        }
    }
}