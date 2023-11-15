package com.example.upschoolcapstoneproject.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.common.strike
import com.example.upschoolcapstoneproject.common.visible
import com.example.upschoolcapstoneproject.data.model.response.ProductUI
import com.example.upschoolcapstoneproject.databinding.ItemProductBinding

class ProductsAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onFavClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, ProductsAdapter.ProductViewHolder>(ProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

                ivFavorite.setBackgroundResource(
                    if (product.isFav) R.drawable.ic_fav_selected
                    else R.drawable.ic_fav_unselected
                )

                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                if (product.saleState) {
                    tvSalePrice.visible()
                    tvPrice.text = "${product.price} ₺"
                    tvSalePrice.text = "${product.salePrice} ₺"
                    tvPrice.strike = true
                    tvPrice.setTextColor(Color.parseColor("#FF0000"))
                } else {
                    tvPrice.text = "${product.price} ₺"
                    tvPrice.strike = false
                    tvSalePrice.gone()
                }

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                ivFavorite.setOnClickListener {
                    onFavClick(product)
                }
            }
        }
    }

    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

}