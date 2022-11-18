package com.mercadolivro.mercadolivro.security

import com.mercadolivro.mercadolivro.exception.AuthenticationException
import com.mercadolivro.mercadolivro.service.UserDetailsCustomService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    authenticationManager : AuthenticationManager,
    private val userDetailsCustomService: UserDetailsCustomService,
    private val jwtUtil: JwtUtil

) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse, chain: FilterChain) {

        //recuperar o nosso token pelo header
        val authorization = request.getHeader("Authorization")

        //verificando se a authorization n eh nula e se começa com Bearer
        if (authorization != null && authorization.startsWith("Bearer ")){
            val auth = getAuthentication(authorization.split(" ")[1]) //pegando o token que vem dps do espaço

            //informando ao spring que quem está acessando eh esse usuário do token
            SecurityContextHolder.getContext().authentication = auth
        }

        //aqui nós vamos passar request e response para a próxima etapa da nossa autenticação
        //para ver se o usuario conseque acessar a url que ele tá chamando
        chain.doFilter(request, response)

    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {

        //se não estiver inválido agora
        if (!jwtUtil.isValidToken(token)){
            throw AuthenticationException("Tokent Inválido", "999")
        }

        //subject eh o identificador do login, nesse caso, o id
        val subject = jwtUtil.getSubject(token)

        //chamando a classe que obtem o customer do bd e já transforma em um userDetails
        val customer = userDetailsCustomService.loadUserByUsername(subject)

        //retornando o token
        return UsernamePasswordAuthenticationToken(customer, null, customer.authorities)
    }

}