package com.mercadolivro.service

import com.mercadolivro.enum.CustomerStatus
import com.mercadolivro.enum.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    val pageable = PageRequest.of(0, 10)

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val customersMock = PageImpl(listOf(buildCustomer(), buildCustomer()), pageable, 2)

        every { customerRepository.findAll(pageable) } returns customersMock

        val customers = customerService.getAll(null, pageable)

        assertEquals(customersMock, customers)

        verify(exactly = 0) { customerRepository.findByNameContaining(any(), any()) }
        verify(exactly = 1) { customerRepository.findAll(pageable) }
    }

    @Test
    fun `should return customers when name is informed`() {
        val name = UUID.randomUUID().toString()
        val customersMock = PageImpl(listOf(buildCustomer(), buildCustomer()), pageable, 2)

        every { customerRepository.findByNameContaining(name, pageable) } returns customersMock

        val customers = customerService.getAll(name, pageable)

        assertEquals(customersMock, customers)

        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNameContaining(any(), any()) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val customer = buildCustomer()
        val password = UUID.randomUUID().toString()

        every { customerRepository.save(any()) } returns customer
        every { bCrypt.encode(any()) } returns password

        customerService.create(customer)

        verify(exactly = 1) { customerRepository.save(any()) }
        verify(exactly = 1) { bCrypt.encode(any()) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random().nextInt()
        val customerMock = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(customerMock)

        val customer = customerService.findById(id)

        assertEquals(customerMock, customer)

        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw exception when not found a customer by id`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.findById(id) }

        assertEquals("Customer with id=$id not exists", error.message)
        assertEquals("ML-201", error.erroCode)

        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random().nextInt()
        val customerMock = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(customerMock) } returns customerMock

        customerService.update(customerMock)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(customerMock) }
    }

    @Test
    fun `should throw nout found exception when update customer`() {
        val id = Random().nextInt()
        val customerMock = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(customerMock) } returns customerMock

        val error = assertThrows<NotFoundException> {
            customerService.update(customerMock)
        }

        assertEquals("Customer with id=$id not exists", error.message)
        assertEquals("ML-201", error.erroCode)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val customerMock = buildCustomer(id = id)
        val expectedCustomer = customerMock.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } returns customerMock
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(customerMock) } just runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(customerMock) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random().nextInt()

        every { customerService.findById(id) } throws NotFoundException(
            Errors.ML201.message.format(id),
            Errors.ML201.code
        )

        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer with id=$id not exists", error.message)
        assertEquals("ML-201", error.erroCode)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = "${Random().nextInt().toString()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should return false when email available`() {
        val email = "${Random().nextInt().toString()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
}