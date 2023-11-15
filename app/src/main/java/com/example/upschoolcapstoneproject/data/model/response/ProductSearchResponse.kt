package com.example.upschoolcapstoneproject.data.model.response

data class ProductSearchResponse(
    val products: List<Product>? = null,
    val status: Int? = null,
    val message: String? = null
)