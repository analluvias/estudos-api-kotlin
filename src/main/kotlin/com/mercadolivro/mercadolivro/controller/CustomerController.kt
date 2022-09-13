package com.mercadolivro.mercadolivro.controller

import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.model.CustomerModel
import com.mercadolivro.mercadolivro.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController //informando que a classe é um controller
@RequestMapping("/customers")//caminho de todo esse controller
class CustomerController(
        @Autowired
        val customerService : CustomerService
) {


    @GetMapping()
    //chamando o service
    fun getAll(@RequestParam name: String?): List<CustomerModel> {
        return customerService.getAll(name)
    }

    //chamando o service
    @PostMapping //caminho na raiz
    @ResponseStatus(HttpStatus.CREATED) //status de criado
    fun create(@RequestBody customer: PostCustomerRequest){
        customerService.create(customer)
    }


    @GetMapping("/{id}")
    fun getCustomer(@PathVariable("id") id: String): CustomerModel{

        return customerService.getCustomer(id)
    }

    //atualiza todos os campos atualizaveis do customer
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun update(@PathVariable("id") id: String,
               @RequestBody customer: PutCustomerRequest){

        customerService.update(id, customer)
    }

    //deleta
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun delete(@PathVariable("id") id: String) {

        customerService.delete(id)
    }

    /*
    COLLECTIONS DO POSTMAN - PERMITEM QUE EU JUNTE TODAS AS MINHAS REQUISIÇÕES EM UMA COLEÇÃO
    PARA QUE EU POSSA GUARDAR AS MINHAS REQUISIÇÕES PARA USAR MAIS TARDE
     */
}