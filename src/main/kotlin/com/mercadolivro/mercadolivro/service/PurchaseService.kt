package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.events.PurchaseEvent
import com.mercadolivro.mercadolivro.model.PurchaseModel
import com.mercadolivro.mercadolivro.repository.PurchaseRespository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository : PurchaseRespository,

    //usaremos para disparar um evento
    private val applicationEventPublisher: ApplicationEventPublisher

) {

    fun create(purchaseModel: PurchaseModel){
        purchaseRepository.save(purchaseModel)

        //aqui vamos publicar um evento que criamos na pasta event
        //mandamos a fonte do evento, no caso (this) e passamos ainda
        //o model de compras (ou seja: a nossa entidade do banco)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

}
