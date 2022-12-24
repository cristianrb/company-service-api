package com.github.cristianrb.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.cristianrb.exceptions.RestExceptionHandler
import com.github.cristianrb.models.CompanyResponse
import com.github.cristianrb.services.CompanyService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.ZonedDateTime
import java.util.*

class CompanyControllerTest {

    private val companyService = mockk<CompanyService>()

    private val mvc = MockMvcBuilders.standaloneSetup(CompanyController(companyService))
        .setControllerAdvice(RestExceptionHandler())
        .setMessageConverters(MappingJackson2HttpMessageConverter())
        .build()

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `GIVEN a company controller WHEN asking for list of companies THEN it should return two companies`() {
        val expectedCompanies = listOf("Microsoft", "Google")
        every { companyService.listCompanies() } returns expectedCompanies

        mvc.perform(
            MockMvcRequestBuilders.get("/companies")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect {
                Assertions.assertEquals(objectMapper.writeValueAsString(expectedCompanies), it.response.contentAsString)
            }
    }

    @Test
    fun `GIVEN a company controller WHEN asking for Google share prices THEN it should return them`() {
        val prices = TreeMap<ZonedDateTime, Double>()
        val expectedCompanyResponse = CompanyResponse("Google", prices)
        every { companyService.infoCompany("Google", "hourly") } returns expectedCompanyResponse

        mvc.perform(
            MockMvcRequestBuilders.get("/companies/Google?timeSeries=hourly")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect {
                Assertions.assertEquals(objectMapper.writeValueAsString(expectedCompanyResponse), it.response.contentAsString)
            }
    }

    @Test
    fun `GIVEN an invalid time series WHEN asking for share prices THEN it should return a bad request error`() {
        mvc.perform(
            MockMvcRequestBuilders.get("/companies/Google?timeSeries=monthly")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}