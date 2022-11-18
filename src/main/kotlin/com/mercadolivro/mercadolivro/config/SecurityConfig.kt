package com.mercadolivro.mercadolivro.config

import com.mercadolivro.mercadolivro.enums.Role
import com.mercadolivro.mercadolivro.repository.CustomerRepository
import com.mercadolivro.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.mercadolivro.security.CustomAuthenticationEntryPoint
import com.mercadolivro.mercadolivro.security.JwtUtil
import com.mercadolivro.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetailsCustomService: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val customEntryPoint: CustomAuthenticationEntryPoint

): WebSecurityConfigurerAdapter() {

    private val PUBLIC_MATCHERS = arrayOf<String>()

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers"
    )

    private val PUBLIC_GET_MATCHERS = arrayOf(
        "/books"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )


    override fun configure(auth: AuthenticationManagerBuilder) {
        //a partir do id, vamos recuperar o usuário (customer)
        //userDetailsCustomService -> classe que criamos que retorna um userDetails a partir do customer
        //além disso, passarmos também o nosso codificador de senhas, para ele aprender como codificar
        //e decodificar a senha recebida
        auth.userDetailsService(userDetailsCustomService).passwordEncoder(bCryptPasswordEncoder())

    }


    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()

        http.authorizeRequests()
            .antMatchers(*PUBLIC_MATCHERS).permitAll()
            .antMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
            .antMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()//esse caminho para post n precisa de autenticação
            .antMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
            .antMatchers("/swagger-ui").permitAll()
            .anyRequest().authenticated()//todas as requests que chegarem vão precisar de autenticação

        //filtro pelo qual nos vamos receber a autenticação e veirificar se existe no bd
        //nos criamos essa AuthenticationFilter dentro da pasta security
        //ela recebe: um authenticationManager, o customerRepository e a classe
        //jwtUtil (que nós criamos) que gera o token
        http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))


        //agora vamos criar um authorizationFilter, que vai verificar se o usuário tem as roles
        //necessárias para acessar um recurso
        //ela recebe: um authenticationManager, o customerRepository e a classe
        //jwtUtil (que nós criamos) que gera o token e faz outras verificações mais simples a partir
        //do token
        http.addFilter(AuthorizationFilter(authenticationManager(), userDetailsCustomService, jwtUtil))

        //as conexões podem ser de usuários diferentes, por isso todos precisam se autenticar
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        //erro que vamos retornar quando der unauthorized
        http.exceptionHandling().authenticationEntryPoint(customEntryPoint)
    }

    //mandando o HttpSecurity ignorar isso aqui e permitir sempre
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/static/css/**, /static/js/**, *.ico");

		// swagger
        web.ignoring().antMatchers("/v2/api-docs",
            "/v3/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/csrf/**");
    }

    @Bean
    fun corsConfig(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }


    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }

    
}