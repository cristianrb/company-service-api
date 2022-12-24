package com.github.cristianrb.repositories

import com.github.cristianrb.exceptions.CompanyNotFoundException
import com.github.cristianrb.models.CompanyInfo
import com.github.cristianrb.models.CompanyResponse
import org.springframework.stereotype.Repository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.random.Random

@Repository
class CompanyRepository(val refreshPeriodSeconds: Long = 2L) {

    private val companies: List<String> = listOf("amazon", "google", "microsoft")
    private var pricesPerCompany: MutableMap<String, CompanyInfo> = mutableMapOf()
    private val defaultInitDate = ZonedDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault())

    fun listCompanies() : List<String> {
        return companies
    }

    fun getSharePrices(name: String): CompanyResponse {
        if (!companies.contains(name.lowercase())) {
            throw CompanyNotFoundException("Company $name not found")
        }

        val companyInfo = pricesPerCompany[name]
        /* TODO: After data is set, next calls should just refresh last value or add a new value if the time
                 since last value is bigger than 5 minutes. Atm it is refreshing the whole list.
         */
        if (companyInfo == null || refreshPeriodExpired(companyInfo.lastUpdated)) {
            pricesPerCompany[name] = CompanyInfo(generateValuesEvery5Minutes(), ZonedDateTime.now())
        }

        val prices = pricesPerCompany[name]?.prices?: TreeMap<ZonedDateTime, Double>()
        return CompanyResponse(name, prices)
    }

    private fun refreshPeriodExpired(lastUpdated: ZonedDateTime) =
        lastUpdated.plusSeconds(refreshPeriodSeconds).isBefore(ZonedDateTime.now())

    private fun generateValuesEvery5Minutes() : TreeMap<ZonedDateTime, Double> {
        val values = TreeMap<ZonedDateTime, Double>()
        var startDate = defaultInitDate

        val endDate = ZonedDateTime.now()

        while (startDate.isBefore(endDate)) {
            values[startDate] = Random.nextDouble(1000.0)
            startDate = startDate.plusMinutes(5)
        }

        return values
    }
}