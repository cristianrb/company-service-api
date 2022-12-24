package com.github.cristianrb.services

import com.github.cristianrb.models.CompanyResponse

interface CompanyService {

    fun listCompanies(): List<String>

    fun infoCompany(name: String, timeSeries: String) : CompanyResponse
}