package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PostBookRequest(

    @field:NotEmpty(message = "Nome deve ser informado")
    val name: String,

    @field:NotNull(message = "Price deve ser informado")
    val price: BigDecimal,

    @JsonAlias("customer_id")
    val customerId: Int

)