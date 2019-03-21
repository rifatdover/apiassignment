package com.zensar.api.client

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class BackendDataExtensionsTest {

    companion object {
        @JvmStatic
        fun displayArguments(): Stream<Arguments> = Stream.of(
                Arguments.of(Price(now = "10", currency = "GBP"), "", "£10"),
                Arguments.of(Price(now = "10.00", currency = "GBP"), "", "£10"),
                Arguments.of(Price(now = "10.2", currency = "EUR"), "", "€10.20"),
                Arguments.of(Price(now = "10.2", currency = "USD"), "", "$10.20"),
                Arguments.of(Price(now = "10.2", currency = "TL"), "", "TL10.20"),
                Arguments.of(Price(then1 = "10.5", now = "10.2", currency = "EUR"), "", "€10.20"),
                Arguments.of(Price(was = "10.5", now = "10.2", currency = "EUR"), "ShowWasNow", "Was €10.50, now €10.20"),
                Arguments.of(Price(was = "10.5", then1 = "10.3", now = "10.2", currency = "EUR"), "ShowWasThenNow", "Was €10.50, then €10.30, now €10.20"),
                Arguments.of(Price(was = "10.5", then1 = "10.3", then2 = "10.35", now = "10.2", currency = "EUR"), "ShowWasThenNow", "Was €10.50, then €10.35, now €10.20"),
                Arguments.of(Price(was = "10.5", then1 = "10.3", then2 = "10.35", now = "10.2", currency = "EUR"), "ShowWasThenNow", "Was €10.50, then €10.35, now €10.20"),
                Arguments.of(Price(was = "10", then1 = "10.3", then2 = "10.35", now = "5", currency = "EUR"), "ShowPercDscount", "%100 off - now €5.00"),
                Arguments.of(Price(now = null, currency = null), "", ""),
                Arguments.of(Price(now = null, currency = null), "ShowWasThenNow", ""),
                Arguments.of(Price(now = null, currency = null), "ShowWasNow", "")
        )

        @JvmStatic
        fun currencyArguments(): Stream<Arguments> = Stream.of(
                Arguments.of(Price(currency = "EUR"), "€"),
                Arguments.of(Price(currency = "USD"), "$"),
                Arguments.of(Price(currency = "GBP"), "£"),
                Arguments.of(Price(currency = "TL"), "TL"),
                Arguments.of(Price(currency = ""), ""),
                Arguments.of(Price(currency = null), "")
        )

        @JvmStatic
        fun valueArguments(): Stream<Arguments> = Stream.of(
                Arguments.of(Price(now = "1"), "1.00"),
                Arguments.of(Price(now = "5.2"), "5.20"),
                Arguments.of(Price(now = "5.24"), "5.24"),
                Arguments.of(Price(now = "10.00"), "10"),
                Arguments.of(Price(now = "10.2"), "10.20"),
                Arguments.of(Price(now = "10.24"), "10.24"),
                Arguments.of(Price(now = "20.0"), "20")
        )

        @JvmStatic
        fun perscDiscount(): Stream<Arguments> = Stream.of(
                Arguments.of(Price(now = "1", was = "5"), "%400"),
                Arguments.of(Price(now = "3.25", was = "6.50"), "%100"),
                Arguments.of(Price(now = "3", was = "6"), "%100"),
                Arguments.of(Price(now = "2.25", was = "6.75"), "%200"),
                Arguments.of(Price(now = "2.25", was = null), ""),
                Arguments.of(Price(now = null, was = null), "")
        )

    }

    @ParameterizedTest(name = "{index} => price={0}, type={1}, result={2}")
    @MethodSource("displayArguments")
    fun `test display`(price: Price, type: String, result: String) {
        assertEquals(price.display(type), result)
    }

    @ParameterizedTest(name = "{index} => price={0}, value={1}")
    @MethodSource("currencyArguments")
    fun `test display currency`(price: Price, value: String) {
        assertEquals(price.displayCurrency(), value)
    }

    @ParameterizedTest(name = "{index} => price={0}, value={2}")
    @MethodSource("valueArguments")
    fun `test display price value`(price: Price, value: String) {
        assertEquals(price.now?.displayPriceValue(), value)
    }

    @ParameterizedTest(name = "{index} => price={0}, value={2}")
    @MethodSource("perscDiscount")
    fun `test display percentage discount`(price: Price, value: String) {
        assertEquals(price.displayPerscDisc(), value)
    }


}


