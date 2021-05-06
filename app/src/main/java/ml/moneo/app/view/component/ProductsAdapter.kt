package ml.moneo.app.view.component

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.model.Product

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    var items: List<Product> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapter.ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindProduct(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ProductViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {

        init {
            itemview.setOnClickListener { onItemClick?.invoke(items[adapterPosition]) }
        }

        fun bindProduct(product: Product) {
            this.itemView.findViewById<TextView>(R.id.product_name).text = product.name

            //TODO: Set image of product async from cache/download
        }

        override fun onClick(v: View?) {
            //itemView.callOnClick()
        }
    }
}