package ru.ifmo.rxweb.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
class User (
        @Id
        val id : Int,
        val name : String,
        val currency : Currency
)


@Document(collection = "products")
class Product(
        @Id
        val id : Int,
        val name : String,
        val price : Double
)

enum class Currency(val ratio : Int) {
        USD(1),
        RUB(23),
        ZMB(500)
}