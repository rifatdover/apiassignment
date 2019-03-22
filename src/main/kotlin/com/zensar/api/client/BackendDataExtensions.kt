package com.zensar.api.client

import com.zensar.api.web.rest.ColorSwatchDTO

/**
 * Extension function for displaying data
 * with given price label type
 * @param type String?
 * @return String
 */
fun Price.display(type: String?): String {
    return when (type) {
        "ShowWasNow" -> {
            if (!now.isNullOrEmpty() && !was.isNullOrEmpty())
                return "Was ${displayCurrency()}${was.displayPriceValue()}, " +
                        "now ${displayCurrency()}${now.displayPriceValue()}"
            return display("")

        }
        "ShowWasThenNow" -> {
            if (!now.isNullOrEmpty() && !was.isNullOrEmpty()) {
                if (!then2.isNullOrEmpty())
                    return "Was ${displayCurrency()}${was.displayPriceValue()}, " +
                            "then ${displayCurrency()}${then2.displayPriceValue()}, " +
                            "now ${displayCurrency()}${now.displayPriceValue()}"

                if (!then1.isNullOrEmpty())
                    return "Was ${displayCurrency()}${was.displayPriceValue()}, " +
                            "then ${displayCurrency()}${then1.displayPriceValue()}, " +
                            "now ${displayCurrency()}${now.displayPriceValue()}"

            }
            return display("")
        }
        "ShowPercDscount" -> {
            if (discount > 0)
                "${displayPerscDisc()} off - now ${displayCurrency()}${now.displayPriceValue()}"
            else display("")
        }
        else -> displayCurrency() + now.displayPriceValue()
    }
}

/**
 * Extension function for displaying currency data.
 *
 * If it is not a known currency it displays it is name.
 * @param type String?
 * @return String
 */
fun Price.displayCurrency() = when (currency) {
    "USD" -> "$"
    "EUR" -> "€"
    "GBP" -> "£"
    null -> ""
    else -> currency
}

/**
 * Extension function for displaying discount value in percent.
 *
 * It only displays value if there is a discount.
 * @return String
 */
fun Price.displayPerscDisc(): String {
    return if (discount > 0) "%${discount.toInt()}" else ""
}

/**
 * Extension property for discount.
 * @return Double
 */
val Price.discount: Double
    get() {
        val nowd = now?.toDoubleOrNull()
        val wasd = was?.toDoubleOrNull()
        if (nowd != null && wasd != null)
            return ((wasd - nowd) / nowd) * 100
        return 0.00
    }
/**
 * Extension property for reduction.
 * @return Double
 */
val Price.reduction: Double
    get() {
        val nowd = now?.toDoubleOrNull()
        val wasd = was?.toDoubleOrNull()
        if (nowd != null && wasd != null)
            return wasd - nowd
        return 0.00
    }

/**
 * Extension function for String values for displaying
 * price values
 * @return String
 */
fun String?.displayPriceValue(): String {
    val double = this?.toDoubleOrNull()
    val int = double?.toInt()
    if (int != null) {
        if (int >= 10 && int.compareTo(double) == 0) {
            return int.toString()
        }
    }
    if (double != null)
        return "%.2f".format(double)
    return ""

}

/**
 * Mapper method from [BackendColorSwatch] to [ColorSwatchDTO].
 */
fun BackendColorSwatch.toColorSwatchDTO(): ColorSwatchDTO = ColorSwatchDTO(color, colorMap[basicColor], skuId)

private val colorMap = hashMapOf(
        "Black" to "000000",
        "Blue" to "0000FF",
        "Green" to "00FF00",
        "Grey" to "808080",
        "Multi" to "000000",
        "Orange" to "FFA500",
        "Pink" to "FFC0CB",
        "Purple" to "800080",
        "Red" to "FF0000",
        "White" to "FFFFFF",
        "Yellow" to "FFFF00"

)