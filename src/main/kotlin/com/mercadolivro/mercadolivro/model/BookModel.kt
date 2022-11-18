package com.mercadolivro.mercadolivro.model

import com.mercadolivro.mercadolivro.enums.BookStatus
import lombok.Data
import java.math.BigDecimal
import javax.persistence.*

@Data
@Entity(name = "book")
data class BookModel(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Int? = null,

        @Column(name = "name")
        var name : String,

        @Column
        var price : BigDecimal,



        //muitos livros pertencem a um usuário
        @ManyToOne
        //informando que o juntamos as tabelas a partir do campo costumer_id
        //QUE EH A FOREIGN KEY NO BD
        @JoinColumn(name="customer_id")
        var costumer : CustomerModel?=null,

        @Column
        @Enumerated(EnumType.STRING)
        var status : BookStatus?=null


){
//        //aqui dessa forma estou criando uma lógica que lança uma exceção se eu tentat
//        //alterar um livro cancelado ou deletado
//        //de outra forma eu vou conseguir alterar
//        @Column
//        @Enumerated(EnumType.STRING)
//        var status : BookStatus?=null
//                set(value) {
//                        if (field == BookStatus.CANCELADO || field == BookStatus.DELETADO){
//                                throw BadRequestException(Errors.ML102.message.format(field), Errors.ML102.code)
//                        }
//                        field = value
//
//                }
//
//        public constructor(id: Int? = null,
//                    name: String,
//                    price: BigDecimal,
//                    customer: CustomerModel? = null,
//                    status: BookStatus?) : this(id, name, price, customer){ this.status = status}


}

