package com.mercadolivro.helper

import com.mercadolivro.enum.CustomerStatus
import com.mercadolivro.enum.Role
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import java.math.BigDecimal
import java.util.*

fun buildCustomer(
    id: Int? = null,
    name: String = "custommer name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "password",
) = CustomerModel(
    id,
    name,
    email,
    CustomerStatus.ATIVO,
    password,
    setOf(Role.CUSTOMER),
)

fun buildPurchase(
    id: Int? = null,
    customer: CustomerModel = buildCustomer(),
    books: MutableList<BookModel> = mutableListOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN
) = PurchaseModel(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)