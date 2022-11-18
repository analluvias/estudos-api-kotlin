package com.mercadolivro.mercadolivro.controller.request

import com.mercadolivro.mercadolivro.validation.EmailAvailble
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty



data class PostCustomerRequest (

        //aqui adicionaremos anotações de validação do spring validation

        @field:NotEmpty(message = "Nome deve ser informado")
        var name : String,

        @field:Email(message = "Email deve ser válido")
        @field:EmailAvailble
        var email: String,

        @field:NotEmpty(message = "Senha deve ser informada")
        var password: String
)