package com.mercadolivro.mercadolivro.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION) //a anotação funciona para funções
@Retention(AnnotationRetention.RUNTIME)
//permitir que o autenticado
//só veja as suas informações no get, MAS SE FOR ADMIN, PODE VER TUDO
@PreAuthorize("hasRole('ROLE_ADMIN') || #id == authentication.principal.id")
annotation class UserCanOnlyAccessTheirOwnResource()
