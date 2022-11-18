package com.mercadolivro.mercadolivro.controller.request

import com.mercadolivro.mercadolivro.validation.EmailAvailble
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest (

        @field:NotEmpty
        var name : String,

        @field:Email(message = "Email deve ser válido")
        @field:EmailAvailble
        var email: String
)