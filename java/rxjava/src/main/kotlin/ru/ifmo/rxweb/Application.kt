package ru.ifmo.rxweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ru.ifmo.rxweb.common", "ru.ifmo.rxweb.master"])
class MasterApplication

fun main(args: Array<String>) {
    MasterRunner.run(args)
}

object MasterRunner {
    fun run(args: Array<String>) {
        runApplication<MasterApplication>(*args)
    }
}