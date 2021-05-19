package ml.moneo.app.viewmodel

import androidx.lifecycle.*
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote

class ProductsViewModel : ViewModel() {
    private val availableProducts = MutableLiveData<List<Remote>>().apply {
        value = getProducts()
    }
    private var selectedProduct = MutableLiveData<Remote>()
    private val productSearchString = MutableLiveData<String>()

    fun getAllProductsBySearch(): LiveData<List<Remote>> {
        return availableProducts
    }

    fun setSelectedProduct(remoteId: Int) {
        selectedProduct.value = availableProducts.value?.find { it.remoteId == remoteId }
    }

    fun getSelectedProduct(): LiveData<Remote> {
        return selectedProduct
    }

    fun getSearchString(): LiveData<String> {
        return productSearchString
    }

    fun searchProducts(searchString: String) {
        val tempProducts = getProducts()

        if (searchString.isEmpty()) {
            availableProducts.postValue(tempProducts)
        } else {
            val filteredProducts =
                tempProducts.filter { it.name.toLowerCase().contains(searchString) }

            availableProducts.postValue(filteredProducts)
        }

        productSearchString.value = searchString
    }

    fun resetSelectedProduct() {
        selectedProduct = MutableLiveData<Remote>()
    }

    private fun getProducts(): List<Remote> {
        return listOf(
            Remote("KPN-remote 1", 1, R.drawable.default_remote),
            Remote("Samsung Universal Remote", 2, R.drawable.default_remote),
            Remote("KPN-Remote 2", 3, R.drawable.default_remote),
        )
    }
}