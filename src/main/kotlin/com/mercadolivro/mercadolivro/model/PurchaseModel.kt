package com.mercadolivro.mercadolivro.model

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity(name = "purchase")
data class PurchaseModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,


    @ManyToOne //muitas comprar para um cliente
    @JoinColumn(name = "customer_id") //chave estrangeira dessa tabela purchase
    val  customer : CustomerModel,

    @ManyToMany//muitos livros para muitos clientes
    @JoinTable(name = "purchase_book", joinColumns = [JoinColumn(name ="purchase_id")],
    inverseJoinColumns = [JoinColumn(name = "book_id")])
    var books: MutableList<BookModel>,

    @Column
    val nfe: String? = null,

    @Column
    val price: BigDecimal,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

)