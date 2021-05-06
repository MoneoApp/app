package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ml.moneo.app.model.Product

class ProductsViewModel : ViewModel() {

    private val products: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>().apply {
            value = listOf(
                Product("Samsung 3212-GHIU-90"),
                Product("KPN Z-NH-TYU"),
                Product("One For All URC6820"),
                Product("Logitech G567"),
                Product("Logitech G609FGH"),
                Product("Samsung 32KL344JK"),
                Product("Logitech GRT676HN"),
                Product("KPN Remoto")
            )
        }
    }

    fun getProducts(): LiveData<List<Product>> {
        return products
    }

}