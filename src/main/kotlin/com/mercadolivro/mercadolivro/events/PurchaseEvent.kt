package com.mercadolivro.mercadolivro.events

import com.mercadolivro.mercadolivro.model.PurchaseModel
import org.springframework.context.ApplicationEvent


//essa clase extende de ApplicationEvent que recebe um source(fonte)
class PurchaseEvent(
    source: Any,
    val purchaseModel: PurchaseModel

) : ApplicationEvent(source)