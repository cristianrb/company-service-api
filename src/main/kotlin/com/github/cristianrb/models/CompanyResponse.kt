package com.github.cristianrb.models

import java.time.ZonedDateTime
import java.util.*

data class CompanyResponse(
    val name: String,
    var prices: TreeMap<ZonedDateTime, Double>,
)