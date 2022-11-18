package com.mercadolivro.mercadolivro.extension

import com.mercadolivro.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.mercadolivro.controller.response.BookResponse
import com.mercadolivro.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.mercadolivro.controller.response.PageResponse
import com.mercadolivro.mercadolivro.enums.BookStatus
import com.mercadolivro.mercadolivro.enums.CustomerStatus
import com.mercadolivro.mercadolivro.model.BookModel
import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page


/*
AQUI EU ESTOU EXTENDENDO A MINHA CLASSE DTO DE POST PARA QUE EU NÃO PRECISE COLOCAR ESSA FUNÇÃO
EXATAMENTE DENTRO DELA, MAS FUNCIONA DA MESMA MANEIRA

EU SÓ PRECISO IMPORTAR ESSA CLASSE LÁ NO MEU CONTROLLER AGORA
 */


fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(
        name = this.name, email = this.email,
        status = CustomerStatus.ATIVO, password = this.password)
}

fun PutCustomerRequest.toCustomerModel(previousValue: CustomerModel): CustomerModel {
    return CustomerModel(
        id = previousValue.id,
        name = this.name,
        email = this.email,
        status = previousValue.status,
        password = previousValue.password
    )
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel{
    return BookModel(
        name=this.name,
        price = this.price,
        status = BookStatus.ATIVO,
        costumer = customer)
}

fun PutBookRequest.toBookModel(previousValue: BookModel): BookModel{
    return BookModel(
        id = previousValue.id,
        name = this.name ?: previousValue.name, //se não passarmos esse param, então vamos deixar o antigo
        price = this.price ?: previousValue.price, //se não passarmos esse param, então vamos deixar o antigo
        status = previousValue.status,
        costumer = previousValue.costumer
    )
}

fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(id = this.id, name = this.name,
        email = this.email, status = this.status)
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(id = this.id, name = this.name, price = this.price,
        costumer = this.costumer, status = this.status)
}


fun <T> Page<T>.toPageResponse(): PageResponse<T>{
    return PageResponse(
        this.content,
        this.number,
        this.totalElements,
        this.totalPages)
}