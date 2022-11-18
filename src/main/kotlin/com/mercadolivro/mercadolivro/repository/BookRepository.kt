package com.mercadolivro.mercadolivro.repository

import com.mercadolivro.mercadolivro.enums.BookStatus
import com.mercadolivro.mercadolivro.model.BookModel
import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.awt.print.Pageable

@Repository
interface BookRepository : JpaRepository<BookModel, Int> {


    fun findByCostumer(customer: CustomerModel): List<BookModel>

    fun findByStatus(status: BookStatus, pageable: org.springframework.data.domain.Pageable): Page<BookModel>

}