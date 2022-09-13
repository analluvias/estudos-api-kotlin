package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service //informando que essa classe é um serviço ao spring
class CustomerService {

    val customers = mutableListOf<CustomerModel>()

    //inserindo um requestParam que eh opcional (por isso o ?)
    fun getAll(name: String?): List<CustomerModel> {
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


    fun create(customer: PostCustomerRequest){

        var id = if(customers.isEmpty()){
            1
        }else{
            customers.last().id.toInt() + 1
        }.toString()

        customers.add(  CustomerModel(id,  customer.name, customer.email)  )
    }


    fun getCustomer(id: String): CustomerModel{

        //se o registro tiver um id igual ao id que peguei da url
        //entao o filter retorna aquele registro
        //além disso, quero q ele retorne o primeiro que for achado
        return customers.filter { it.id == id }.first()
    }


    //faz update de todos os campos atualizaveis pelo id
    fun update(id: String,
               customer: PutCustomerRequest){

        customers.filter { it.id == id }.first().let {
            it.name = customer.name
            it.email = customer.email
        }
    }


    //deleta
    fun delete(id: String) {

        //removo se o id do registro for igual ao que eu enviei pela url
        customers.removeIf { it.id == id }
    }

}