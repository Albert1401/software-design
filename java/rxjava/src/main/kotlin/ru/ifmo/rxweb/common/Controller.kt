package ru.ifmo.rxweb.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono
import ru.ifmo.rxweb.model.Product
import ru.ifmo.rxweb.model.ProductRepository
import ru.ifmo.rxweb.model.User
import ru.ifmo.rxweb.model.UserRepository
import sun.plugin2.message.Message
import java.util.stream.Collectors


@Component
class Controller {

    private lateinit var userRepo: UserRepository

    private lateinit var productRepo: ProductRepository

    @Autowired
    constructor(userRepo: UserRepository, productRepo: ProductRepository) {
        this.userRepo = userRepo
        this.productRepo = productRepo
    }



    fun addUser(request: ServerRequest): Mono<ServerResponse> {
        return request.body(BodyExtractors.toMono(User::class.java))
                .flatMap { u ->
                    userRepo.existsById(u.id).flatMap {
                        if (it) {
                            status(HttpStatus.BAD_REQUEST).message("User with such id already exists")
                        } else {
                            userRepo.insert(u).flatMap {
                                status(HttpStatus.CREATED).message("OK")
                            }
                        }
                    }
                }
    }


    fun addProduct(request: ServerRequest): Mono<ServerResponse> {
        return request.body(BodyExtractors.toMono(Product::class.java))
                .flatMap { p ->
                    productRepo.existsById(p.id).flatMap {
                        if (it) {
                            status(HttpStatus.BAD_REQUEST).message("Product with such id already exists")
                        } else {
                            productRepo.insert(p).flatMap {
                                status(HttpStatus.CREATED).message("OK")
                            }
                        }
                    }
                }

    }

    fun showProducts(request: ServerRequest): Mono<ServerResponse> {
        return request.queryParam("userId")
                .map {
                    userRepo.findById(Integer.parseInt(it))
                            .flatMap { u ->
                                productRepo.findAll().map {
                                    Product(it.id, it.name, it.price * u.currency.ratio)
                                }
                                        .collect(Collectors.toList())
                                        .flatMap { ok().json(it) }
                            }
                }
                .orElse(status(HttpStatus.BAD_REQUEST).json("No user with such id "))
    }
}

fun ServerResponse.BodyBuilder.json(): ServerResponse.BodyBuilder =
        contentType(MediaType.APPLICATION_JSON_UTF8)

fun <T> ServerResponse.BodyBuilder.json(value: T): Mono<ServerResponse> = json().body(BodyInserters.fromObject(value))

fun ServerResponse.BodyBuilder.message(s : String) : Mono<ServerResponse> = json(Response(s))