package com.github.cristianrb.services.impl

import com.github.cristianrb.services.TimeSeriesService
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

@Service
class TimeSeriesServiceImpl: TimeSeriesService {

    override fun transformTo(prices: TreeMap<ZonedDateTime, Double>, timeSeries: String): TreeMap<ZonedDateTime, Double> {
        val transformedValues = TreeMap<ZonedDateTime, Double>()
        var sum = 0.0
        var count = 0
        var previousKey: ZonedDateTime? = null
        for (entry in prices) {
            if (previousKey == null) {
                previousKey = entry.key
            }
            sum += entry.value
            count++

            when (timeSeries) {
                "weekly" -> if (previousKey.dayOfWeek != entry.key.dayOfWeek && entry.key.dayOfWeek == DayOfWeek.MONDAY) {
                    val previousMonday = entry.key.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
                    transformedValues[previousMonday.truncatedTo(ChronoUnit.DAYS)] = sum/count
                    sum = 0.0
                    count = 0
                }
                "daily" -> if (previousKey.dayOfMonth != entry.key.dayOfMonth) {
                    transformedValues[previousKey.truncatedTo(ChronoUnit.DAYS)] = sum/count
                    sum = 0.0
                    count = 0
                }
                "hourly" -> if (previousKey.hour != entry.key.hour) {
                    transformedValues[previousKey.withMinute(0)] = sum/count
                    sum = 0.0
                    count = 0
                }
            }

            previousKey = entry.key
        }

        // Compute last pending values
        if (timeSeries == "weekly") {
            previousKey = previousKey!!.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        }

        if (timeSeries == "hourly") {
            transformedValues[previousKey!!.withMinute(0)] = sum/count
        } else {
            transformedValues[previousKey!!.truncatedTo(ChronoUnit.DAYS)] = sum / count
        }

        return transformedValues
    }
}