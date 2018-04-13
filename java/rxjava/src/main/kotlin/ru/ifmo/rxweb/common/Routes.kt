package ru.ifmo.rxweb.common

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class Routes {
    @Bean
    fun mainRouter(
            handler: Controller
    ) = router {
        "/users/add".nest {
            POST("/", handler::addUser)
        }

        "/products".nest {
            GET("/show", handler::showProducts)
            POST("/add", handler::addProduct)
        }
    }
}