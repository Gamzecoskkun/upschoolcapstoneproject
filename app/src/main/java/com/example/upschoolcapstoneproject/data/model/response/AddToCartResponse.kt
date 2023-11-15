package com.example.upschoolcapstoneproject.data.model.response

data class AddToCartResponse(
    val id: Int? = null,
    val title: String? = null,
    val price: Double? = null,
    val salePrice: Double? = null,
    val description: String? = null,
    val category: String? = null,
    val imageOne: String? = null,
    val imageTwo: String? = null,
    val imageThree: String? = null,
    val rate: Double? = null,
    val count: Int? = null,
    val saleState: Boolean? = null,
): BaseResponse()