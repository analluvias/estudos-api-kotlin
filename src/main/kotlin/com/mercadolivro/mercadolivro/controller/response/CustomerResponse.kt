package com.mercadolivro.mercadolivro.controller.response

import com.mercadolivro.mercadolivro.enums.CustomerStatus
import javax.persistence.*

data class CustomerResponse(

    var id : Int? = null,

    var name : String,

    var email: String,

    var status: CustomerStatus
) {

}
