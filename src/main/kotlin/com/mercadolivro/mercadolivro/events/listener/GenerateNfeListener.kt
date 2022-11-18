package com.mercadolivro.mercadolivro.events.listener

import com.mercadolivro.mercadolivro.events.PurchaseEvent
import com.mercadolivro.mercadolivro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID


//criando a classe que vai gerar a nossa listener
//que ouvirá quando uma compra for criada e vai
//mandar gerar uma nota fiscal
@Component
class GenerateNfeListener(
    private val purchaseService: PurchaseService
) {

    //gerando um código aleatório para a nossa nota fiscal
    //fazendo uma cópia da purchaseModel que o eventListener armazena
    //já setando a nfe dela para a que geramos aqui
    @Async
    @EventListener
    fun listen(purchaseEvent: PurchaseEvent){
        val nfe = UUID.randomUUID().toString()

        val purchaseModel = purchaseEvent.purchaseModel.copy( nfe = nfe)

        purchaseService.update(purchaseModel)
    }


}
