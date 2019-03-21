package com.zensar.api.web.rest

data class ProductDTO(val productId: String,
                      val title: String,
                      val colorSwatches: List<ColorSwatchDTO>?,
                      val nowPrice: String, val priceLabel: String)

data class ColorSwatchDTO(val color: String,
                          val rgbColor: String?,
                          val skuid: String)
