package ml.moneo.app.viewmodel

import androidx.lifecycle.*
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.moneo.DeviceByIdQuery
import ml.moneo.DeviceManualsByDeviceIdQuery
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Product
import ml.moneo.app.model.Remote
import ml.moneo.app.util.apolloClient

class ProductsViewModel : ViewModel() {
    private val availableProducts = MutableLiveData<List<Remote>>()
    private var selectedProduct = MutableLiveData<Remote>()
    private val productSearchString = MutableLiveData<String>()

    fun getAllProductsBySearch(): LiveData<List<Remote>> {
        return availableProducts
    }

    fun setSelectedProduct(remoteId: String) {
        selectedProduct.value = availableProducts.value?.find { it.remoteId == remoteId }
    }

    fun getSelectedProduct(): LiveData<Remote> {
        return selectedProduct
    }

    fun getSearchString(): LiveData<String> {
        return productSearchString
    }

    fun searchProducts(searchString: String) {
//        val tempProducts = getProducts()
//
//        if (searchString.isEmpty()) {
//            availableProducts.postValue(tempProducts)
//        } else {
//            val filteredProducts =
//                tempProducts.filter { it.name.toLowerCase().contains(searchString) }
//
//            availableProducts.postValue(filteredProducts)
//        }
//
//        productSearchString.value = searchString
    }

    fun resetSelectedProduct() {
        selectedProduct = MutableLiveData<Remote>()
    }

    fun getProducts(deviceIds: List<String>) {
        val client = apolloClient()
        val tempList: MutableList<Remote> = mutableListOf()

        GlobalScope.launch {
            deviceIds.forEach {
                val response = try {
                    client.query(DeviceByIdQuery(it)).await()
                } catch (e: ApolloException) {
                    return@launch
                }

                val device = response.data?.device
                if (device == null || response.hasErrors()) {
                    println(response.errors?.firstOrNull()?.message);
                    return@launch
                }

                tempList.add(Remote(device.model, device.id, R.drawable.default_remote))
            }

            availableProducts.postValue(tempList)
        }
    }
}