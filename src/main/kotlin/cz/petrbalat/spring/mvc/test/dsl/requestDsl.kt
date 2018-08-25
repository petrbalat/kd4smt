package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@DslMarker
annotation class RequestDsl

@DslMarker
annotation class ResultDsl

fun MockMvc.request(method: HttpMethod, url: String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    val request = DslRequestBuilder(MockMvcRequestBuilders.request(method, url)).apply(block)
    val result = this.perform(request.buildRequest())
    return request.applyResult(result).andReturn()
}

fun MockHttpServletRequestBuilder.jsonContent(jsonContent: String) {
    content(jsonContent)
    contentType(MediaType.APPLICATION_JSON)
    accept(MediaType.APPLICATION_JSON)
}