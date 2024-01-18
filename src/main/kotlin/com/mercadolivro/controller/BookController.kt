package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.PageResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toPageResponse
import com.mercadolivro.extension.toResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("books")
class BookController(
    private val bookService: BookService,
    private val customerService: CustomerService,
) {

    @GetMapping
    fun findAll(
        @PageableDefault(page = 0, size = 10)
        pageable: Pageable
    ): PageResponse<BookResponse> =
        bookService.findAll(pageable).map { it.toResponse() }.toPageResponse()

    @GetMapping("/active")
    fun findAllActive(
        @PageableDefault(page = 0, size = 10)
        pageable: Pageable
    ): Page<BookResponse> =
        bookService.findAllActive(pageable).map { it.toResponse() }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable(name = "id")
        id: Int,
    ): BookResponse = bookService.findById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody
        @Valid
        request: PostBookRequest,
    ) {
        val customer = customerService.findById(request.customerId)

        bookService.createBook(request.toBookModel(customer))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable(name = "id")
        id: Int
    ) = bookService.delete(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable(name = "id")
        id: Int,
        @RequestBody
        request: PutBookRequest,
    ) {
        val book = bookService.findById(id)

        bookService.update(request.toBookModel(book))
    }
}