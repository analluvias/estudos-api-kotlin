package com.mercadolivro.mercadolivro.model

import com.mercadolivro.mercadolivro.enums.CustomerStatus
import com.mercadolivro.mercadolivro.enums.Role
import lombok.Data
import javax.persistence.*

@Data
@Entity(name = "customer")
data class CustomerModel(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        @Column(name = "name")
        var name: String,

        @Column(name="email")
        var email: String,

        @Column
        @Enumerated(EnumType.STRING)
        var status: CustomerStatus,

        @Column
        val password: String,

        //collection table eh uma tabela que não tem id
        //aqui nessa anotação estamos informando que "role" não está dentro da tabela de
        //customer, mas sim na tabela de role
        //vamos juntas a partir da tabela customer id, vamos saber se a informação eh desse
        //customer a partir do id
        @CollectionTable(name="customer_roles", joinColumns = [JoinColumn(name = "customer_id")])
        //essa anotação aqui basicamente diz que sempre que eu buscar um customer
        //eutambém quero que busque as informações na tabela de customer_role (que eh um enum "Profile")
        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @Column(name= "role")
        @Enumerated(EnumType.STRING)
        var roles: Set<Role> = setOf()
)