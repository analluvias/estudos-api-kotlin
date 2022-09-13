package com.mercadolivro.mercadolivro.controller

import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController //informando que a classe é um controller
@RequestMapping("/customers")//caminho de todo esse controller
class CustomerController {

    val customers = mutableListOf<CustomerModel>()

    @GetMapping()
    //inserindo um requestParam que eh opcional (por isso o ?)
    fun getAll(@RequestParam name: String?): List<CustomerModel> {
        //so fazemos essa busca se o nome for diferente de null

        //ou seja: se enviamos o requestparam para buscar por nome
        //o codigo vai permitir que iteremos a lista e verifiquemos se
        //em cada nome contem o valor buscado, se contiver, a gente retorna eles
        //em uma lista, por isso o tipo de retorno da fun eh uma lista
        name?.let {
            return customers.filter {
                it.name.contains(name, true)
            }
        }

        //se o parametro estiver nulo, retornamos todos da lista
        return customers
    }

    //criamos um DTO dentro da pasta request (eh um customerModel, mas sem o id)
    //para que não passemos o id pro post
    @PostMapping //caminho na raiz
    @ResponseStatus(HttpStatus.CREATED) //status de criado
    fun create(@RequestBody customer: PostCustomerRequest){

        var id = if(customers.isEmpty()){
            1
        }else{
            customers.last().id.toInt() + 1
        }.toString()

        customers.add(  CustomerModel(id,  customer.name, customer.email)  )
    }


    @GetMapping("/{id}")
    fun getCustomer(@PathVariable("id") id: String): CustomerModel{

        //se o registro tiver um id igual ao id que peguei da url
        //entao o filter retorna aquele registro
        //além disso, quero q ele retorne o primeiro que for achado
        return customers.filter { it.id == id }.first()
    }

    //atualiza todos os campos atualizaveis do customer
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun update(@PathVariable("id") id: String,
               @RequestBody customer: PutCustomerRequest){

        customers.filter { it.id == id }.first().let {
            it.name = customer.name
            it.email = customer.email
        }
    }

    //atualiza todos os campos atualizaveis do customer
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // FEITO COM SUCESSO, MAS NÃO RETORNA NADA
    fun update(@PathVariable("id") id: String) {

        //removo se o id do registro for igual ao que eu enviei pela url
        customers.removeIf { it.id == id }
    }

    /*
    COLLECTIONS DO POSTMAN - PERMITEM QUE EU JUNTE TODAS AS MINHAS REQUISIÇÕES EM UMA COLEÇÃO
    PARA QUE EU POSSA GUARDAR AS MINHAS REQUISIÇÕES PARA USAR MAIS TARDE
     */
}