package com.mercadolivro.service

import com.mercadolivro.enum.BookStatus
import com.mercadolivro.enum.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository
) {
    fun findAll(pageable: Pageable): Page<BookModel> = bookRepository.findAll(pageable)

    fun createBook(book: BookModel) = bookRepository.save(book)

    fun findAllActive(pageable: Pageable): Page<BookModel> = bookRepository.findAllByStatus(BookStatus.ATIVO, pageable)

    fun findById(id: Int): BookModel =
        bookRepository.findById(id)
            .orElseThrow {
                NotFoundException(
                    Errors.ML101.message.format(id),
                    Errors.ML101.code
                )
            }

    fun update(book: BookModel) = bookRepository.save(book)

    fun delete(id: Int) {
        val book = findById(id)

        book.status = BookStatus.DELETADO

        update(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)

        for (book in books) {
            book.status = BookStatus.DELETADO
        }

        bookRepository.saveAll(books)
    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map {
            it.status = BookStatus.VENDIDO
        }

        bookRepository.saveAll(books)
    }
}