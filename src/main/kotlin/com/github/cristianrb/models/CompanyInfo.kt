package com.github.cristianrb.models

import java.time.ZonedDateTime
import java.util.*

data class CompanyInfo(
    var prices: TreeMap<ZonedDateTime, Double>,
    val lastUpdated: ZonedDateTime
)