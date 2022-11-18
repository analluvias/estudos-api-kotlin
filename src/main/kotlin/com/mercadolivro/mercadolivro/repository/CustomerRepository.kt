package com.mercadolivro.mercadolivro.repository

import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
//@Component("customerRepository")
interface CustomerRepository : JpaRepository<CustomerModel, Int>{

    fun existsByEmail(email : String): Boolean

    //nullable ppois o email pode n existir
    fun findByEmail(email : String): CustomerModel?


    fun findByNameContaining(name: String) : List<CustomerModel>
}