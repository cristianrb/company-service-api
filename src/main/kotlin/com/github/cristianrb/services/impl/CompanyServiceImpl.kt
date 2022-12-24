package com.github.cristianrb.services.impl

import com.github.cristianrb.models.CompanyResponse
import com.github.cristianrb.repositories.CompanyRepository
import com.github.cristianrb.services.CompanyService
import com.github.cristianrb.services.TimeSeriesService
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository,
    private val timeSeriesServiceImpl: TimeSeriesService
): CompanyService {

    override fun listCompanies(): List<String> {
        return companyRepository.listCompanies()
    }

    override fun infoCompany(name: String, timeSeries: String): CompanyResponse {
        val companyResponse = companyRepository.getSharePrices(name)
        companyResponse.prices = timeSeriesServiceImpl.transformTo(companyResponse.prices, timeSeries)
        return companyResponse
    }

}