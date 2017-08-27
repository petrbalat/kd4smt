package cz.petrbalat.spring.mvc.test.dsl.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HelloController {

    @GetMapping("/hello")
    fun hello(@RequestParam name: String, modelMap: ModelMap): String {
        modelMap["name"] = name
        return "hello"
    }

    @PostMapping("/hello")
    fun helloPost(dto: HelloPostDto, modelMap: ModelMap): String = "hello"

}

data class HelloPostDto(var surname:String = "World")