package com.mercadolivro.repository

import com.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface CustomerRepository : JpaRepository<CustomerModel, Int> {

    fun findByNameContaining(it: String, pageable: Pageable): Page<CustomerModel>

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): CustomerModel?

}