package cz.petrbalat.spring.mvc.test.dsl.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*

@Controller
class HelloController {

    @GetMapping("/hello")
    fun hello(@RequestParam name: String, modelMap: ModelMap): String {
        modelMap["name"] = name
        return "hello"
    }

    @GetMapping("/hello/{name}")
    @ResponseBody
    fun helloJson(@PathVariable name: String) = HelloPostDto(surname = name)

    @PostMapping("/hello")
    fun helloPost(dto: HelloPostDto, modelMap: ModelMap): String = "Hello ${dto.surname}"

    @PutMapping("/hello")
    @ResponseBody
    fun helloPut(@RequestBody dto: HelloPostDto, @CookieValue cookieName: String) = ResponseEntity.badRequest().body(dto.copy(extraName = cookieName))

}

data class HelloPostDto(var surname:String = "World", val extraName:String? = null)