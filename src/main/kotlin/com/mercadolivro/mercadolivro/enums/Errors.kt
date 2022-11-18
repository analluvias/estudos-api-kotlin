package com.mercadolivro.mercadolivro.enums

enum class Errors(
    val code: String,
    val message: String
) {
    ML000("ML000", "ACCESS DENIED"),
    ML001("ML001", "INVALID REQUEST"),
    ML101("ML-101", "Book %s não existe"),
    ML102("ML-102", "Não posso dar update no livo com status"),
    ML201("ML-201", "Customer %s não existe")
}