package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer")
class CostumerController(
    val customerService: CustomerService,
) {

    @GetMapping
    fun findByName(
        @RequestParam name: String?,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<CustomerResponse> = customerService.findByName(name, pageable).map { it.toResponse() }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable(name = "id") id: Int,
    ): CustomerResponse = customerService.findById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody @Valid request: PostCustomerRequest,
    ) = customerService.create(request.toCustomerModel())

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable(name = "id") id: Int,
        @RequestBody @Valid request: PutCustomerRequest,
    ) {
        val customer = customerService.findById(id)

        customerService.update(request.toCustomerModel(customer))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable(name = "id") id: Int,
    ) = customerService.delete(id)
}