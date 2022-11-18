package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.helper.*
import com.mercadolivro.mercadolivro.enums.CustomerStatus
import com.mercadolivro.mercadolivro.enums.Errors
import com.mercadolivro.mercadolivro.exception.NotFoundException
import com.mercadolivro.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
internal class CustomerServiceTest{

    //nesses @MockK criamos os parametros mockados (de mentirinha)
    //para o customer service
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK //permite que eu mocke métodos de dentro do meu próprio customerService
    //aqui criamos o customerService que vai ter como parametros
    //as classes que mockamos acima
    private lateinit var customerService: CustomerService


    @Test
    fun `should return all customers`(){

        //lista de customers de mentirinhas
        val fakeCustomer = listOf(buildCustomer(), buildCustomer())

        //sempre que o meu repositório for chamado, retorne a lista falsa
        every { customerRepository.findAll() } returns fakeCustomer

        val customers = customerService.getAll(null)

        //valor esperado, valor recebido
        assertEquals(fakeCustomer, customers)

        //verificando se esse customerRepository.findAll só foi chamado uma vez
        verify(exactly = 1) { customerRepository.findAll() }


        //como não passamos nenhum nome, queremos ver se ele vai chamar o
        //findByNameContaining(any()) contendo qualquer nome dentro
        //no caso, ele não deve chamar esse método nenhuma vez, pois chamamos o getAll()
        //sem nenhum parametro dentro dele
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `should return all customers when name is informed`(){

        val name = "Gustavo"

        //lista de customers de mentirinhas
        val fakeCustomer = listOf(buildCustomer(), buildCustomer())

        //sempre que o meu repositório for chamado, retorne a lista falsa
        every { customerRepository.findByNameContaining(name) } returns fakeCustomer


        //guardando o resultado retornado pelo service
        val customers = customerService.getAll(name)

        //valor esperado, valor recebido
        assertEquals(fakeCustomer, customers)

        //verificando se esse customerRepository.findAll nao foi chamado
        verify(exactly = 0) { customerRepository.findAll() }


        //verificando se o findByName foi chamado 1 vez com o valor name
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
    }

    //vamos testas o customerService.create() que funciona fazendo uma
    //copia encriptada dp customer e depois dalvando esta no repositório
    @Test
    fun `should create customer and encrypt password`(){
        val initialPassword = Math.random().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrypted) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        //verificanfo se chamei .encode() uma vez
        verify(exactly = 1) { bCrypt.encode(initialPassword) }

        //verificando se chamei .save() uma vez
        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypted) }
    }


    //teste do getById, testando o caminho feliz
    @Test
    fun `should return customer by id`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.getById(id)

        //esperado, recebido
        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    //caminho triste
    @Test
    fun `should throwr notFound error when find by id`(){
        val id = Random().nextInt()

        //queremos que retornemos um optional vazio, pois nao foi encontrado
        every { customerRepository.findById(id) } returns Optional.empty()

        //chamando getById() e capturanto o erro
        //garantindo que o erro lançado foi um NotFoundException
        val error = assertThrows<NotFoundException> {
            customerService.getById(id)
        }

        //verificando se a mensagem eh a esperada
        assertEquals("Customer $id não existe", error.message)

        //verificando se o erro eh o esperado
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerRepository.findById(id) }
    }


    //caminho feliz should update customer
    @Test
    fun `should update customer`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer


        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }

    }

    @Test
    fun `should throw not found exception when update customer`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException> {
            customerService.update(fakeCustomer)
        }

        //verificando se a mensagem eh a esperada
        assertEquals("Customer $id não existe", error.message)

        //verificando se o erro eh o esperado
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerRepository.existsById(id) }

        //não vai chamar o .save() nenhuma vez
        verify(exactly = 0) { customerRepository.save(any()) }

    }


    @Test
    fun `should delete customer`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        //verificando se existe
        every { customerService.getById(id) } returns fakeCustomer

        //usamos o just runs quando o retorno do método eh void
        //deletando todos os livros desse customer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs

        //retornando o expected customer (que não eh excluido), mas inativado
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer

        customerService.delete(id)

        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete customer`(){
        val id = Random().nextInt()

        //quando nao existe estoura uma exceção
        every { customerService.getById(id) } throws
                NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)


        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }



        verify(exactly = 1) { customerService.getById(id) }
        //sempre que eu colocar exactly = 0, eu uso o any()
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }


    @Test
    fun `should return true when email availble`(){
        val email = "${Random().nextInt().toString()}@email.com"

        //vendo que o email nao existe, portando eh possívl usá-lo
        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should return false when email unavailble`(){
        val email = "${Random().nextInt().toString()}@email.com"

        //vendo que o email ja existe, portando nao eh possívl usá-lo
        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }



//    @Test
//    fun `fake test`(){
//        //esperado, atual
//        assertEquals(2, 2)
//    }


}