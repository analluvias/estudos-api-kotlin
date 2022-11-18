package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.events.PurchaseEvent
import com.mercadolivro.mercadolivro.helper.buildPurchase
import com.mercadolivro.mercadolivro.repository.PurchaseRespository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var purchaseRepository : PurchaseRespository

    @MockK
    private  lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService


    private val purchaseEventSlot = slot<PurchaseEvent>()


    //---------USO DO SLOT AQUI----------------
    @Test
    fun `should create purchase and publish event`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(purchase)

        verify(exactly = 1) {purchaseRepository.save(purchase)}

        //verificando se o .publishEvent() foi chamado uma vez e
        //quando publishEvent for chamado vamos guardar o nosso evento dentro desse "buraco"
        verify(exactly = 1) {applicationEventPublisher.publishEvent(capture(purchaseEventSlot))}

        //verificando se esse evento eh o que a gente estava esperando
        //então, esperamos uma purchase, veremos se o purchaseModel do evento
        //capturado eh a nossa purchase enviada
        assertEquals(purchase, purchaseEventSlot.captured.purchaseModel)

    }


    @Test
    fun `should UPDATE purchase`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        purchaseService.update(purchase)

        verify(exactly = 1) {purchaseRepository.save(purchase)}
    }

}