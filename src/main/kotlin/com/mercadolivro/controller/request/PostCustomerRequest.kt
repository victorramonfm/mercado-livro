package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PostCustomerRequest(

    @field:NotEmpty(message = "Nome deve ser informado")
    val name: String,

    @field:Email(message = "Email deve ser válido")
    @EmailAvailable
    val email: String,

    @field:NotEmpty(message = "Senha deve ser informada")
    val password: String
)