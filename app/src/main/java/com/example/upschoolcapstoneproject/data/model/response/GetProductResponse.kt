package com.example.upschoolcapstoneproject.data.model.response

data class GetProductResponse(
    val products: List<Product>?
):BaseResponse()