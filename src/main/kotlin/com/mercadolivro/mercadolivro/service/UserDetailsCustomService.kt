package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.exception.AuthenticationException
import com.mercadolivro.mercadolivro.repository.CustomerRepository
import com.mercadolivro.mercadolivro.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
): UserDetailsService {

    //recebemos um id e por ele vamos no bd buscar um customer
    // e retornamos um userdetails (ou seja)
    // o que o spring boot considera um login
    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow { AuthenticationException("Usuario não encontrado", "999") }

        //transformando o nosso user em um UserCustomDetails (que extende de UserDetails)
        //e retornando
        return UserCustomDetails(customer)
    }


}