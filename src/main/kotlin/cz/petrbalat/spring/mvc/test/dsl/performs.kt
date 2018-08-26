package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.net.URI


//GET
fun MockMvc.performGet(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.get(url), block)
}

fun MockMvc.performGet(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.get(uri), block)
}

//POST
fun MockMvc.performPost(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.post(url), block)
}

fun MockMvc.performPost(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.post(uri), block)
}

//PUT
fun MockMvc.performPut(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.put(url), block)
}

fun MockMvc.performPut(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.put(uri), block)
}

//DELETE
fun MockMvc.performDelete(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.delete(url), block)
}

fun MockMvc.performDelete(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.delete(uri), block)
}

//HEAD
fun MockMvc.performHead(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.head(url), block)
}

fun MockMvc.performHead(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.head(uri), block)
}

//PATCH
fun MockMvc.performPatch(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.patch(url), block)
}

fun MockMvc.performPatch(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.patch(uri), block)
}

//OPTIONS
fun MockMvc.performOptions(url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.options(url), block)
}

fun MockMvc.performOptions(uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.options(uri), block)
}

//REQUEST
fun MockMvc.perform(method: HttpMethod, url: String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.request(method, url), block)
}

fun MockMvc.perform(method: HttpMethod, uri: URI, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.request(method, uri), block)
}

private fun performDsl(mockMvc: MockMvc,
                       requestBuilder: MockHttpServletRequestBuilder,
                       block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    val request = DslRequestBuilder(requestBuilder).apply(block)
    val result = mockMvc.perform(request.buildRequest())
    return request.applyResult(result).andReturn()
}