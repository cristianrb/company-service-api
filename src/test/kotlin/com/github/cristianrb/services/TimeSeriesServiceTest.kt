package com.github.cristianrb.services

import com.github.cristianrb.services.impl.TimeSeriesServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*
import kotlin.random.Random

class TimeSeriesServiceTest {

    private val timeSeriesService = TimeSeriesServiceImpl()

    @Test
    fun `GIVEN month of January 2022 WHEN transforming to hourly THEN we should get 744 values`() {
        val initDate = ZonedDateTime.parse("2022-01-01T00:00:00+01:00[Europe/Paris]")
        val endDate = ZonedDateTime.parse("2022-02-01T00:00:00+01:00[Europe/Paris]")
        val prices = generateValuesEvery5Minutes(initDate, endDate)
        val hourlyPrices = timeSeriesService.transformTo(prices, "hourly")

        Assertions.assertEquals(prices.size / 12, hourlyPrices.size)
    }

    @Test
    fun `GIVEN month of January 2022 WHEN transforming to daily THEN we should get 31 values`() {
        val initDate = ZonedDateTime.parse("2022-01-01T00:00:00+01:00[Europe/Paris]")
        val endDate = ZonedDateTime.parse("2022-02-01T00:00:00+01:00[Europe/Paris]")
        val prices = generateValuesEvery5Minutes(initDate, endDate)
        val hourlyPrices = timeSeriesService.transformTo(prices, "daily")

        Assertions.assertEquals(prices.size / 12 / 24, hourlyPrices.size)
    }

    @Test
    fun `GIVEN month of January 2022 WHEN transforming to weekly THEN we should get 6 values`() {
        val initDate = ZonedDateTime.parse("2022-01-01T00:00:00+01:00[Europe/Paris]")
        val endDate = ZonedDateTime.parse("2022-02-02T00:00:00+01:00[Europe/Paris]")
        val prices = generateValuesEvery5Minutes(initDate, endDate)
        val hourlyPrices = timeSeriesService.transformTo(prices, "weekly")

        // Jan 2022 has 5 Mondays + 27/12/2021 as we have to compute days 1 and 2 from 2022 which are Saturday and Sunday
        Assertions.assertEquals(6, hourlyPrices.size)
    }

    private fun generateValuesEvery5Minutes(initDate: ZonedDateTime, endDate: ZonedDateTime) : TreeMap<ZonedDateTime, Double> {
        val values = TreeMap<ZonedDateTime, Double>()

        var startDate = initDate
        while (startDate.isBefore(endDate)) {
            values[startDate] = Random.nextDouble(1000.0)
            startDate = startDate.plusMinutes(5)
        }

        return values
    }
}