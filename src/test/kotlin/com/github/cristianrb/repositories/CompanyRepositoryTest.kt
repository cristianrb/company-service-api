package com.github.cristianrb.repositories

import com.github.cristianrb.exceptions.CompanyNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompanyRepositoryTest {

    private val companyRepository = CompanyRepository()
    private val companyRepositoryWithSmallRefreshPeriod = CompanyRepository(0)

    @Test
    fun `GIVEN a valid company name WHEN requesting prices THEN it should return a list with time and value`() {
        val companyResponse = companyRepository.getSharePrices("Microsoft")

        assert("Microsoft" == companyResponse.name)
        Assertions.assertTrue(companyResponse.prices.isNotEmpty())
    }

    @Test
    fun `GIVEN a non existing company name WHEN requesting prices THEN it should throw an error`() {
        assertThrows<CompanyNotFoundException> {
            companyRepository.getSharePrices("Twitter")
        }
    }

    @Test
    fun `GIVEN a valid company name WHEN requesting prices twice within refresh period THEN it should return the same list`() {
        val companyResponse = companyRepository.getSharePrices("Microsoft")
        val companyResponse2 = companyRepository.getSharePrices("Microsoft")

        Assertions.assertEquals(companyResponse, companyResponse2)
    }

    @Test
    fun `GIVEN a valid company name WHEN requesting prices twice with expired refresh period THEN it should return a different list`() {
        val companyResponse = companyRepositoryWithSmallRefreshPeriod.getSharePrices("Microsoft")
        val companyResponse2 = companyRepositoryWithSmallRefreshPeriod.getSharePrices("Microsoft")

        Assertions.assertNotEquals(companyResponse, companyResponse2)
    }

    @Test
    fun `GIVEN a company repository WHEN requesting list of companies THEN it should return three items`() {
        val companies = companyRepository.listCompanies()

        Assertions.assertEquals(3, companies.size)
    }
}