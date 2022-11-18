package com.mercadolivro.mercadolivro.service

import com.mercadolivro.mercadolivro.enums.BookStatus
import com.mercadolivro.mercadolivro.enums.Errors
import com.mercadolivro.mercadolivro.exception.NotFoundException
import com.mercadolivro.mercadolivro.model.BookModel
import com.mercadolivro.mercadolivro.model.CustomerModel
import com.mercadolivro.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository
) {
    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return bookRepository.findAll(pageable)
    }

    fun findActives(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun findById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow{
            NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code)
        }
    }

    //não não vamos querer apagar nunca um livro do bd
    //apenas mudar seu status para cancelado
    fun delete(id: Int) {
        val book = findById(id)

        book.status = BookStatus.CANCELADO

        update(book)
    }

    fun update(bookModel: BookModel) {
        bookRepository.save(bookModel)
    }

    //não vamos deletar os livros de fato, apenas mudar o status
    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCostumer(customer)

        for (book in books){
            book.status = BookStatus.DELETADO
        }

        bookRepository.saveAll(books)
    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map {
            it.status = BookStatus.VENDIDO
        }

        bookRepository.saveAll(books)
    }

}