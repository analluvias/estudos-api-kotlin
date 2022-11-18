package com.mercadolivro.mercadolivro.controller

import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.mercadolivro.extension.toCustomerModel
import com.mercadolivro.mercadolivro.extension.toResponse
import com.mercadolivro.mercadolivro.security.UserCanOnlyAccessTheirOwnResource
import com.mercadolivro.mercadolivro.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController //informando que a classe é um controller
@RequestMapping("/customers")//caminho de todo esse controller
class CustomerController(
        @Autowired
        private val customerService : CustomerService
) {


    @GetMapping()
    //chamando o service
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {
        return customerService.getAll(name).map { it.toResponse() }
    }

    //chamando o service
    @PostMapping //caminho na raiz
    @ResponseStatus(HttpStatus.CREATED) //status de criado
    fun create(@RequestBody @Valid customer: PostCustomerRequest){
        customerService.create(  customer.toCustomerModel()  )
    }


    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getCustomer(@PathVariable("id") id: Int): CustomerResponse{

        return customerService.getById(id).toResponse()
    }

    //atualiza todos os campos atualizaveis do customer
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun update(@PathVariable("id")id: Int,
               @RequestBody @Valid customer: PutCustomerRequest){

        val customerSaved = customerService.getById(id)

        customerService.update(customer.toCustomerModel(customerSaved))
    }

    //deleta
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun delete(@PathVariable("id") id: Int) {

        customerService.delete(id)
    }

    /*
    COLLECTIONS DO POSTMAN - PERMITEM QUE EU JUNTE TODAS AS MINHAS REQUISIÇÕES EM UMA COLEÇÃO
    PARA QUE EU POSSA GUARDAR AS MINHAS REQUISIÇÕES PARA USAR MAIS TARDE
     */

    /*
    PS. EH INTERESSANTE QUE OS NOSSOS SERVICES RECEBAM APENAS OS MODELS (OU ENTITIES), NÃO OS NOSSOS DTOS
     */
}