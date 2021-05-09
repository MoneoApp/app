package ml.moneo.app.view.component

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.ProductsCatalogFragment
import ml.moneo.app.model.Product

class CatalogAdapter : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    var items: List<Product> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogAdapter.CatalogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
        return CatalogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bindProduct(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CatalogViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {

        init {
            itemview.setOnClickListener { onItemClick?.invoke(items[adapterPosition]) }
        }

        fun bindProduct(product: Product) {
            this.itemView.findViewById<TextView>(R.id.product_name).text = product.name

            //TODO: Set image of product async from cache/download
            this.itemView.findViewById<ImageView>(R.id.product_image).setImageResource(product.resId)
        }

        override fun onClick(v: View?) {
            //TODO: Set selected color
        }
    }
}