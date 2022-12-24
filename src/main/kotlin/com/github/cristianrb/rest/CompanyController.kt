package com.github.cristianrb.rest

import com.github.cristianrb.exceptions.InvalidTimeSeriesException
import com.github.cristianrb.models.CompanyResponse
import com.github.cristianrb.services.CompanyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/companies")
class CompanyController(private val companyService: CompanyService) {

    @GetMapping
    fun listCompanies() : List<String> {
        return companyService.listCompanies()
    }

    @GetMapping("/{companyName}")
    fun infoCompany(
        @PathVariable("companyName") companyName: String,
        @RequestParam timeSeries: String
    ): CompanyResponse {
        val validTimeSeries = listOf("hourly", "daily", "weekly")
        if (!validTimeSeries.contains(timeSeries.lowercase())) {
            throw InvalidTimeSeriesException()
        }
        return companyService.infoCompany(companyName, timeSeries)
    }

}