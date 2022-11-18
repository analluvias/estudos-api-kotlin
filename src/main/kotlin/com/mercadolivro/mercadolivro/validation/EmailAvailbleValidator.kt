package com.mercadolivro.mercadolivro.validation

import com.mercadolivro.mercadolivro.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

//classe que vai validar a nossa anotação
class EmailAvailbleValidator(var customerService: CustomerService)
    : ConstraintValidator<EmailAvailble, String> {


    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()){
            return false
        }

        //acessando o service para poder acessar o bd e verificar se o email esta disponível
        return customerService.emailAvailable(value)
    }
}