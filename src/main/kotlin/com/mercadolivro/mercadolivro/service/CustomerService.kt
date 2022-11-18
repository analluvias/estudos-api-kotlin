package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.enums.CustomerStatus
import com.mercadolivro.mercadolivro.enums.Errors
import com.mercadolivro.mercadolivro.enums.Role
import com.mercadolivro.mercadolivro.exception.NotFoundException
import com.mercadolivro.mercadolivro.model.CustomerModel
import com.mercadolivro.mercadolivro.repository.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service //informando que essa classe é um serviço ao spring
class CustomerService(
        private val customerRepository: CustomerRepository,
        private val bookService: BookService,
        private val bCrypt: BCryptPasswordEncoder
        ) {

    val customers = mutableListOf<CustomerModel>()

    //inserindo um requestParam que eh opcional (por isso o ?)
    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(name);
        }

        return customerRepository.findAll()
    }


    fun create(customer: CustomerModel){
        val customerCopy = customer.copy(
            roles = setOf(Role.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customerCopy)

    }


    fun getById(id: Int): CustomerModel{
        return customerRepository.findById(id).orElseThrow{
            NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)
        }
    }


    //faz update de todos os campos atualizaveis pelo id
    fun update(customer: CustomerModel){

        if (! customerRepository.existsById(customer.id!!)){
            throw NotFoundException(Errors.ML201.message.format(customer.id), Errors.ML201.code)
        }

        customerRepository.save(customer)
    }


    //deleta
    fun delete(id: Int) {

        val customer = getById(id)

        bookService.deleteByCustomer(customer)

        customer.status = CustomerStatus.INATIVO

        customerRepository.save(customer)
    }


    //verificando se o email que passamos para o customer já existe no banco
    //retornamos true se ele não existir, aí estará disponível
    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }

}