package com.example.upschoolcapstoneproject.data.model.response

data class GetCartProductResponse(
    val products: List<Product>?
):BaseResponse()