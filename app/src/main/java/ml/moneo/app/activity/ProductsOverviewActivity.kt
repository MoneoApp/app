package ml.moneo.app.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.databinding.ActivityProductOverviewBinding
import ml.moneo.app.model.Product
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

        productsViewModel.getProducts().observe(this, { products ->
            setupRecyclerview()
            productsAdapter.apply { items = products }
        })
    }

    private fun setupRecyclerview() {
        productsRecyclerView = binding.productsRecyclerview
        val gridLayoutManager = GridLayoutManager(this, gridRowCount)
        productsRecyclerView.layoutManager = gridLayoutManager
        productsAdapter = ProductsAdapter()
        productsRecyclerView.adapter = productsAdapter
    }
}