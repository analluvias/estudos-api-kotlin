package com.mercadolivro.mercadolivro.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.mercadolivro.controller.request.LoginRequest
import com.mercadolivro.mercadolivro.exception.AuthenticationException
import com.mercadolivro.mercadolivro.repository.CustomerRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    //método tentativa de autenticação
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse): Authentication {

        try{//transformando o tipo que eu li da entrada (login e senha) em um objeto LoginRequest
            val loginRequest = jacksonObjectMapper().readValue(request.inputStream, LoginRequest::class.java)

            //recuperando o id deste loginRequest no banco de dados
            val id = customerRepository.findByEmail(loginRequest.email)?.id

            //criando o token passando o id do usuario e a sua senha (que ele já passou para
            //se autenticar) lá pelo postman
            val authToken = UsernamePasswordAuthenticationToken(id, loginRequest.password)

            //pedindo ao spring para validar o token que nós acabamos de criar
            return authenticationManager.authenticate(authToken)
        }catch (ex: Exception){
            throw AuthenticationException("falha ao autenticar", "999")
        }
    }

    //verificando se deu sucesso na autenticação
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {

        //esse authResult é justamente o userCustomDetails que retornamos de attemptAuthentication
        //vamos só fazer um downasting para o objeto do tipo UserCustomDetails (classe que criamos
        //que extende UserDetails) para obter o id da pessoa autenticada
        val id = (authResult.principal as UserCustomDetails).id

        //gerando o token e passando o id, que é o que vai nos ajudar
        //a identificar o log
        val token = jwtUtil.generateToken(id)

        //vamos gerar o token e retornar ele para o usuário (no postman)
        response.addHeader("Authorization", "Bearer $token")

    }

}