package com.mercadolivro.controller.response

data class ErrorResponse(
    var status: Int,
    val message: String,
    var internalCode: String,
    var errors: List<FieldErrorResponse>?
)