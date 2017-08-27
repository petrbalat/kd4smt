package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class KD4SMTApplication

fun main(args: Array<String>) {
    SpringApplication.run(KD4SMTApplication::class.java, *args)
}
