package com.github.cristianrb.services

import java.time.ZonedDateTime
import java.util.*

interface TimeSeriesService {

    fun transformTo(prices: TreeMap<ZonedDateTime, Double>, timeSeries: String): TreeMap<ZonedDateTime, Double>

}