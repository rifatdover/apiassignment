package com.zensar.api.web.rest

/**
 * Data Transfer Object for product.
 */
data class ProductDTO(val productId: String,
                      val title: String,
                      val colorSwatches: List<ColorSwatchDTO>?,
                      val nowPrice: String, val priceLabel: String)

/**
 * Data Transfer Object for color swatch.
 */
data class ColorSwatchDTO(val color: String,
                          val rgbColor: String?,
                          val skuid: String)
