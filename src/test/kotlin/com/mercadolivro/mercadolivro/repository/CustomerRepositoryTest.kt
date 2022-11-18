package com.mercadolivro.mercadolivro.repository

import com.mercadolivro.mercadolivro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.test.context.ActiveProfiles

@SpringBootTest //como o teste envolve um repositório, devemos colocar essa notação
@ExtendWith(MockKExtension::class)
@EnableAspectJAutoProxy(proxyTargetClass=true) //essa anotação resolveu o erro do bean
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    //@Qualifier("customerRepository")
    private lateinit var customerRepository: CustomerRepository

    //antes de cada teste delete tudo do banco
    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `should return name containing`(){
        val marcos = customerRepository.save(buildCustomer(name = "Marcos"))
        val alex = customerRepository.save(buildCustomer(name = "Alex"))
        val matheus = customerRepository.save(buildCustomer(name = "Matheus"))

        val customers = customerRepository.findByNameContaining("Ma")

        Assertions.assertEquals(listOf(marcos.name, matheus.name), listOf(customers[1].name, customers[0].name))
    }

    @Nested
    inner class `exists by email`{
        @Test
        fun `should return true when email exists`(){
            val email = "email@teste.com"

            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            Assertions.assertTrue(exists)
        }

        @Test
        fun `should return false when email do not exists`(){
            val email = "email@teste.com"

            val exists = customerRepository.existsByEmail(email)

            Assertions.assertFalse(exists)
        }
    }


    @Nested
    inner class `find by email`{
        @Test
        fun `should return customer when email exists`(){
            val email = "email@teste.com"

            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            Assertions.assertNotNull(customer)
            Assertions.assertEquals(customer, result)
        }

        @Test
        fun `should return null when email do not exists`(){
            val email = "email@teste.com"

            val result = customerRepository.findByEmail(email)

            Assertions.assertNull(result)
        }
    }

}