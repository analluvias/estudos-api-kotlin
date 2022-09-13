package com.mercadolivro.mercadolivro.controller.request

import com.mercadolivro.mercadolivro.model.CustomerModel

data class PostCustomerRequest (
        var name : String,

        var email: String
){
    fun toCustomerModel(): CustomerModel{
        return CustomerModel(name = this.name, email = this.email)
    }
}