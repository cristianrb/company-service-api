package com.github.cristianrb.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(CompanyNotFoundException::class)
    fun notFoundExceptionHandler(exception: CompanyNotFoundException): ResponseEntity<ErrorBody> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorBody(HttpStatus.NOT_FOUND.value(), exception.message?:"Resource not found"))
    }

    @ExceptionHandler(InvalidTimeSeriesException::class)
    fun invalidTimeSeriesExceptionHandler(exception: InvalidTimeSeriesException): ResponseEntity<ErrorBody> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorBody(HttpStatus.BAD_REQUEST.value(), exception.message?:"The time series is not valid"))
    }

}

class CompanyNotFoundException(msg: String = "The company you requested is not present") : RuntimeException(msg)
class InvalidTimeSeriesException(msg: String = "The time series is not valid") : RuntimeException(msg)

data class ErrorBody(val code: Int, val message: String)