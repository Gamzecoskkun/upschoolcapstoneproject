package com.example.upschoolcapstoneproject.data.source.remote

import com.example.upschoolcapstoneproject.data.model.request.AddToCartRequest
import com.example.upschoolcapstoneproject.data.model.request.ClearCartRequest
import com.example.upschoolcapstoneproject.data.model.request.DeleteFromCartRequest
import com.example.upschoolcapstoneproject.data.model.request.DeleteFromFavoritesRequest
import com.example.upschoolcapstoneproject.data.model.response.AddToCartResponse
import com.example.upschoolcapstoneproject.data.model.response.ClearCartResponse
import com.example.upschoolcapstoneproject.data.model.response.DeleteFromCartResponse
import com.example.upschoolcapstoneproject.data.model.response.GetCartProductResponse
import com.example.upschoolcapstoneproject.data.model.response.GetProductDetailResponse
import com.example.upschoolcapstoneproject.data.model.response.GetProductResponse
import com.example.upschoolcapstoneproject.data.model.response.GetSaleProductResponse
import com.example.upschoolcapstoneproject.data.model.response.ProductSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("get_products.php")
    suspend fun getProducts(): Response<GetProductResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<GetProductDetailResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(): Response<GetSaleProductResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<AddToCartResponse>

    @POST("delete_from_cart.php")
    suspend fun deleteFromCart(
        @Body request: DeleteFromCartRequest
    ): Response<DeleteFromCartResponse>

    @GET("get_cart_products.php")
    suspend fun getCartProducts(
        @Query("userId") userId: String
    ): Response<GetCartProductResponse>

    @POST("clear_cart.php")
    suspend fun clearCart(
        @Body request: ClearCartRequest
    ): Response<ClearCartResponse>

    @POST("delete_from_favorites.php")
    suspend fun deleteFromFavorites(
        @Body request: DeleteFromFavoritesRequest
    ): Response<DeleteFromFavoritesRequest>

    @GET("search_product.php")
    suspend fun searchProduct(
        @Query("query") query: String
    ): Response<ProductSearchResponse>


}