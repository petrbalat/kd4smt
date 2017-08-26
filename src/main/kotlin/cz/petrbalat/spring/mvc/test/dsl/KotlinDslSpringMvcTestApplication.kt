package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinDslSpringMvcTestApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinDslSpringMvcTestApplication::class.java, *args)
}
