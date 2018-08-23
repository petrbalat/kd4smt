package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.net.URI


//GET
fun MockMvc.performGet(url:String, 
                       requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                       init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.get(url), requestInit, init)
}

fun MockMvc.performGet(uri: URI,
                       requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                       init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.get(uri), requestInit, init)
}

//POST
fun MockMvc.performPost(url:String, 
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.post(url), requestInit, init)
}

fun MockMvc.performPost(uri: URI,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.post(uri), requestInit, init)
}

//PUT
fun MockMvc.performPut(url:String,
                       requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                       init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.put(url), requestInit, init)
}

fun MockMvc.performPut(uri: URI,
                       requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                       init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.put(uri), requestInit, init)
}

//DELETE
fun MockMvc.performDelete(url:String,
                          requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                          init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.delete(url), requestInit, init)
}

fun MockMvc.performDelete(uri: URI,
                          requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                          init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.delete(uri), requestInit, init)
}

//HEAD
fun MockMvc.performHead(url:String,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.head(url), requestInit, init)
}

fun MockMvc.performHead(uri: URI,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.head(uri), requestInit, init)
}

//PATCH
fun MockMvc.performPatch(url:String,
                         requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                         init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.patch(url), requestInit, init)
}

fun MockMvc.performPatch(uri: URI,
                         requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                         init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.patch(uri), requestInit, init)
}

//OPTIONS
fun MockMvc.performOptions(url:String,
                           requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                           init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.options(url), requestInit, init)
}

fun MockMvc.performOptions(uri: URI,
                           requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                           init: ResultActions.() -> Unit): MvcResult {
    return performDsl(this, MockMvcRequestBuilders.options(uri), requestInit, init)
}

private fun performDsl(mockMvc: MockMvc,
                       request: MockHttpServletRequestBuilder,
                       requestInit: MockHttpServletRequestBuilder.() -> Unit,
                       init: ResultActions.() -> Unit): MvcResult {
    request.requestInit()
    val perform = mockMvc.perform(request)
    perform.init()
    return perform.andReturn()
}