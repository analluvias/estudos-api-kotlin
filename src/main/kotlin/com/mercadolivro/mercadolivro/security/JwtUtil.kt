package com.mercadolivro.mercadolivro.security

import com.mercadolivro.mercadolivro.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}") //variável que está no meu application.properties
    private val expiration: Long? = null

    @Value("\${jwt.secret}") //variável que está no meu application.properties
    private val secret: String? = null

    //geranto o nosso token de fato
    fun generateToken(id: Int): String {
        return Jwts.builder()
            .setSubject(id.toString()) //como vamos identificar o usuário, pelo id, nesse caso
            .setExpiration(Date(System.currentTimeMillis() + expiration!!)) //passando o tempo atual em
            // milisegundos + o tempo de expiração em milisegundos
            .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray()) //passando o algoritmo de
                //codificação da assinatura do nosso webtoken + a assinatura em um array de bytes
            .compact()

    }


    //verificando se o token está válido atualmente
    fun isValidToken(token: String): Boolean {
        //primeiro vamos recuperar as claims que são as informações contidas no token
        val claims = getClaims(token)

        //verificando se a claim esta nula, sem data de expiração ou expirada
        if (claims.subject == null || claims.expiration == null ||
                Date().after(claims.expiration)){
            return false
        }

        return true

    }


    //recuperando as claims (informações uteis do nosso token)
    private fun getClaims(token: String) : Claims {
        try {
            //pegando as claims
            return Jwts.parser().setSigningKey(secret!!.toByteArray())
                .parseClaimsJws(token).body


        }catch (ex : Exception){
            throw AuthenticationException("Token inválido", "999")
        }
    }

    fun getSubject(token: String) : String {
        return getClaims(token).subject
    }


}