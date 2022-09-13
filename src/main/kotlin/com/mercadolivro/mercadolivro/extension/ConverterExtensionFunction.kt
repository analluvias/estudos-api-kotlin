package com.mercadolivro.mercadolivro.extension

import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.model.CustomerModel


/*
AQUI EU ESTOU EXTENDENDO A MINHA CLASSE DTO DE POST PARA QUE EU NÃO PRECISE COLOCAR ESSA FUNÇÃO
EXATAMENTE DENTRO DELA, MAS FUNCIONA DA MESMA MANEIRA

EU SÓ PRECISO IMPORTAR ESSA CLASSE LÁ NO MEU CONTROLLER AGORA
 */


fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name, email = this.email)
}

fun PutCustomerRequest.toCustomerModel(id: String): CustomerModel {
    return CustomerModel(id = id, name = this.name, email = this.email)
}