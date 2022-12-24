package com.github.cristianrb.services

import com.github.cristianrb.models.CompanyResponse
import com.github.cristianrb.repositories.CompanyRepository
import com.github.cristianrb.services.impl.CompanyServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class CompanyServiceTest {

    @MockK
    lateinit var companyRepository: CompanyRepository

    @MockK
    lateinit var timeSeriesService: TimeSeriesService

    @InjectMockKs
    lateinit var companyService: CompanyServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GIVEN a company service WHEN asking for list of companies THEN it should return 2 companies`() {
        val expectedCompanies = listOf("Microsoft", "Google")
        every { companyRepository.listCompanies() } returns expectedCompanies
        val actualCompanies = companyService.listCompanies()

        Assertions.assertEquals(expectedCompanies, actualCompanies)
    }

    @Test
    fun `GIVEN a company service WHEN asking for share prices of a company THEN it should return its prices`() {
        val prices = TreeMap<ZonedDateTime, Double>()
        val hourlyPrices = TreeMap<ZonedDateTime, Double>()

        val expectedCompanyResponse = CompanyResponse("Google", prices)
        every { companyRepository.getSharePrices("Google") } returns expectedCompanyResponse
        every { timeSeriesService.transformTo(prices, "hourly") } returns hourlyPrices

        val actualCompanyResponse = companyService.infoCompany("Google", "hourly")

        Assertions.assertEquals(expectedCompanyResponse, actualCompanyResponse)
    }

}