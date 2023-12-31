package com.mercadolivro.enums

enum class Errors(val code: String, val message: String) {


    ML001("ML-001", "Invalid Request"),

    ML101("ML-101", "Book with id=%s not exists"),

    ML102("ML-102", "Cannot update book with status=%s"),

    ML201("ML-201", "Customer with id=%s not exists"),

}