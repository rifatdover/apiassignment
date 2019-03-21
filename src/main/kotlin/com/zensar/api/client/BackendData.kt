package com.zensar.api.client


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Price(@JsonProperty("then2")
                 val then2: String? = "",
                 @JsonProperty("then1")
                 val then1: String? = "",
                 @JsonProperty("now")
                 val now: String? = "",
                 @JsonProperty("was")
                 val was: String? = "",
                 @JsonProperty("currency")
                 val currency: String? = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class BackendProducts(@JsonProperty("redirectUrl")
                           val redirectUrl: String? = "",
                           @JsonProperty("selectedDept")
                           val selectedDept: String? = "",
                           @JsonProperty("categoryTitle")
                           val categoryTitle: String? = "",
                           @JsonProperty("childCategoriesCount")
                           val childCategoriesCount: Int? = 0,
                           @JsonProperty("products")
                           val products: List<ProductsItem?>?,
                           @JsonProperty("endecaCanonical")
                           val endecaCanonical: String? = "",
                           @JsonProperty("pagesAvailable")
                           val pagesAvailable: Int? = 0,
                           @JsonProperty("multiCatSelected")
                           val multiCatSelected: String? = "",
                           @JsonProperty("seoBannerId")
                           val seoBannerId: String? = "",
                           @JsonProperty("results")
                           val results: Int? = 0,
                           @JsonProperty("dynamicBannerId")
                           val dynamicBannerId: String? = "")

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductsItem(@JsonProperty("isBundle")
                        val isBundle: Boolean? = false,
                        @JsonProperty("compare")
                        val compare: Boolean? = false,
                        @JsonProperty("code")
                        val code: String? = "",
                        @JsonProperty("categoryQuickViewEnabled")
                        val categoryQuickViewEnabled: Boolean? = false,
                        @JsonProperty("defaultSkuId")
                        val defaultSkuId: String? = "",
                        @JsonProperty("colorWheelMessage")
                        val colorWheelMessage: String? = "",
                        @JsonProperty("colorSwatches")
                        val colorSwatches: List<BackendColorSwatch?>?,
                        @JsonProperty("displaySpecialOffer")
                        val displaySpecialOffer: String? = "",
                        @JsonProperty("type")
                        val type: String? = "",
                        @JsonProperty("title")
                        val title: String? = "",
                        @JsonProperty("reviews")
                        val reviews: Int? = 0,
                        @JsonProperty("price")
                        val price: Price?,
                        @JsonProperty("swatchCategoryType")
                        val swatchCategoryType: String? = "",
                        @JsonProperty("averageRating")
                        val averageRating: Double? = 0.0,
                        @JsonProperty("directorate")
                        val directorate: String? = "",
                        @JsonProperty("outOfStock")
                        val outOfStock: Boolean? = false,
                        @JsonProperty("isProductSet")
                        val isProductSet: Boolean? = false,
                        @JsonProperty("brand")
                        val brand: String? = "",
                        @JsonProperty("availabilityMessage")
                        val availabilityMessage: String? = "",
                        @JsonProperty("image")
                        val image: String? = "",
                        @JsonProperty("isMadeToMeasure")
                        val isMadeToMeasure: Boolean? = false,
                        @JsonProperty("colorSwatchSelected")
                        val colorSwatchSelected: Int? = 0,
                        @JsonProperty("releaseDateTimestamp")
                        val releaseDateTimestamp: Int? = 0,
                        @JsonProperty("productId")
                        val productId: String? = "",
                        @JsonProperty("swatchAvailable")
                        val swatchAvailable: Boolean? = false,
                        @JsonProperty("isInStoreOnly")
                        val isInStoreOnly: Boolean? = false,
                        @JsonProperty("emailMeWhenAvailable")
                        val emailMeWhenAvailable: Boolean? = false,
                        @JsonProperty("fabric")
                        val fabric: String? = "",
                        @JsonProperty("ageRestriction")
                        val ageRestriction: Int? = 0,
                        @JsonProperty("nonPromoMessage")
                        val nonPromoMessage: String? = "")

data class BackendColorSwatch(@JsonProperty("basicColor")
                              val basicColor: String = "",
                              @JsonProperty("isAvailable")
                              val isAvailable: Boolean = false,
                              @JsonProperty("colorSwatchUrl")
                              val colorSwatchUrl: String = "",
                              @JsonProperty("color")
                              val color: String = "",
                              @JsonProperty("imageUrl")
                              val imageUrl: String = "",
                              @JsonProperty("skuId")
                              val skuId: String = "")