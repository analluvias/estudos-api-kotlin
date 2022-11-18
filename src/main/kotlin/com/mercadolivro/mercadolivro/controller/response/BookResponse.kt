package com.mercadolivro.mercadolivro.controller.response

import com.mercadolivro.mercadolivro.enums.BookStatus
import com.mercadolivro.mercadolivro.model.CustomerModel
import java.math.BigDecimal
import javax.persistence.*

data class BookResponse(
    var id : Int? = null,

    var name : String,

    var price : BigDecimal,

    var costumer : CustomerModel?=null,

    var status : BookStatus?=null

) {

}
