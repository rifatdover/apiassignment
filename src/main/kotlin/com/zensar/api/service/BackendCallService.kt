package com.zensar.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.zensar.api.client.*
import com.zensar.api.config.ApplicationProperties
import com.zensar.api.web.rest.ProductDTO
import org.springframework.stereotype.Service

/**
 * Backend call service for managing backend calls and receiving responses.
 */
@Service
class BackendCallService(objectMapper: ObjectMapper, applicationProperties: ApplicationProperties) {

    val backendAPI: BackendApi = BackendApiClientFactory(
            objectMapper,
            applicationProperties.appBackend,
            applicationProperties.key
    ).api

    /**
     * Returns list of products filtering with reduction applied. And also sorts them reduction rate.
     */
    suspend fun getProducts(path: String = "600001506", labelType: String?): List<ProductDTO>? {
        return backendAPI.getProducts(path)
                .await()
                .products
                ?.filter {
                    it?.price != null && it.price.reduction > 0
                }?.sortedByDescending {
                    it!!.price!!.reduction
                }?.mapNotNull { item ->
                    item?.let { productItem ->
                        val colorSwatchList = productItem.colorSwatches?.mapNotNull { it?.toColorSwatchDTO() }
                        ProductDTO(productItem.productId.orEmpty(),
                                productItem.title.orEmpty(), colorSwatchList,
                                productItem.price?.display("").orEmpty(),
                                productItem.price?.display(labelType).orEmpty())
                    }

                }
    }

}