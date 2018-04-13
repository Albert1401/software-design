package ru.ifmo.rxweb.model

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface  UserRepository : ReactiveMongoRepository<User, Int>

@Repository
interface  ProductRepository : ReactiveMongoRepository<Product, Int>

//@Repository
//interface  UserRepository : ReactiveMongoRepository<User, Int>