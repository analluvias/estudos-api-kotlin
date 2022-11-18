package com.mercadolivro.mercadolivro.security

import com.mercadolivro.mercadolivro.enums.CustomerStatus
import com.mercadolivro.mercadolivro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

//classe que recebe um customer model e transforma em userDetails
class UserCustomDetails(val customerModel: CustomerModel): UserDetails {

    val id: Int = customerModel.id!!

    //nesse caso vamos pegar todas as roles do usuário e transformar em um objeto to tipo
    //SimpleGrantedAuthority, retornando uma lista destas
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return customerModel.roles.map { SimpleGrantedAuthority(it.description) }.toMutableList()
    }

    override fun getPassword(): String {
        return customerModel.password
    }

    //nesse caso, nós estamos tratando o id do usuario como se fosse o username, então
    override fun getUsername(): String {
        return customerModel.id.toString()
    }

    //nesse caso só retornamos true
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    //nesse caso só retornamos true
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    //nesse caso só retornamos true
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    //estará permitido se o status dele for "ATIVO"
    override fun isEnabled(): Boolean {
        return customerModel.status == CustomerStatus.ATIVO
    }


}