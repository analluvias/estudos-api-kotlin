package com.mercadolivro.mercadolivro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController //informando que a classe é um controller
@RequestMapping("/admin")//caminho de todo esse controller
class AdminController(
) {


    @GetMapping("/report")
    //chamando o service
    fun report(): String {
        return "This is a report. Only admin can see it"
    }

}