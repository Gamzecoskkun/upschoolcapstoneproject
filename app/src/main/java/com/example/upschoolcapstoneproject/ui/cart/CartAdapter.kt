package com.example.upschoolcapstoneproject.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.common.strike
import com.example.upschoolcapstoneproject.common.visible
import com.example.upschoolcapstoneproject.data.model.response.ProductUI
import com.example.upschoolcapstoneproject.databinding.ItemCartProductBinding

class CartAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<ProductUI, CartAdapter.CartViewHolder>(CartDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onDeleteClick
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CartViewHolder(
        private val binding: ItemCartProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onDeleteClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                ivDelete.setOnClickListener {
                    onDeleteClick(product.id)
                }

                if(product.saleState) {
                    tvSalePrice.visible()
                    tvPrice.text = "${product.price} ₺"
                    tvSalePrice.text = "${product.salePrice} ₺"
                    tvPrice.strike = true
                    tvPrice.setTextColor(Color.parseColor("#FF0000"))
                } else{
                    tvPrice.text = "${product.price} ₺"
                    tvPrice.strike = false
                    tvSalePrice.gone()
                }
            }
        }
    }
}

class CartDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
    override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem == newItem
    }
}

