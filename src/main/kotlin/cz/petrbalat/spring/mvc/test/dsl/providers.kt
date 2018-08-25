package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import java.net.URI

interface MockMvcProvider {

    val mockMvc: MockMvc get

}

//GET
fun MockMvcProvider.performGet(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performGet(url = url, block = block)
}

fun MockMvcProvider.performGet(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performGet(uri = uri, block = block)
}

//POST
fun MockMvcProvider.performPost(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPost(url = url, block = block)
}

fun MockMvcProvider.performPost(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPost(uri = uri, block = block)
}

//PUT
fun MockMvcProvider.performPut(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPut(url = url, block = block)
}

fun MockMvcProvider.performPut(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPut(uri = uri, block = block)
}

//DELETE
fun MockMvcProvider.performDelete(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performDelete(url = url, block = block)
}

fun MockMvcProvider.performDelete(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performDelete(uri = uri, block = block)
}

//HEAD
fun MockMvcProvider.performHead(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performHead(url = url, block = block)
}

fun MockMvcProvider.performHead(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performHead(uri = uri, block = block)
}

//PATCH
fun MockMvcProvider.performPatch(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPatch(url = url, block = block)
}

fun MockMvcProvider.performPatch(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performPatch(uri = uri, block = block)
}

//OPTIONS
fun MockMvcProvider.performOptions(url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performOptions(url = url, block = block)
}

fun MockMvcProvider.performOptions(uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.performOptions(uri = uri, block = block)
}

//REQUESTS
fun MockMvcProvider.perform(method: HttpMethod, url: String, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.perform(method = method, url = url, block = block)
}

fun MockMvcProvider.perform(method: HttpMethod, uri: URI, block: DslRequestBuilder.() -> Unit = {}) {
    this.mockMvc.perform(method = method, uri = uri, block = block)
}