package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.net.URI

//GET
fun MockMvc.performGet(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.get(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performGet(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.get(uri))
    perform.init()
    return perform.andReturn()
}

//POST
fun MockMvc.performPost(url:String, vararg uriVars:Any,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    val request = MockMvcRequestBuilders.post(url, uriVars)
    request.requestInit()

    val perform = this.perform(request)
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performPost(uri: URI,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit): MvcResult {
    val request = MockMvcRequestBuilders.post(uri)
    request.requestInit()
    val perform = this.perform(request)
    perform.init()
    return perform.andReturn()
}

//PUT
fun MockMvc.performPut(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.put(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performPut(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.put(uri))
    perform.init()
    return perform.andReturn()
}

//DELETE
fun MockMvc.performDelete(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.delete(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performDelete(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.delete(uri))
    perform.init()
    return perform.andReturn()
}

//HEAD
fun MockMvc.performHead(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.head(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performHead(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.head(uri))
    perform.init()
    return perform.andReturn()
}

//PATCH
fun MockMvc.performPatch(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.patch(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performPatch(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.patch(uri))
    perform.init()
    return perform.andReturn()
}

//OPTIONS
fun MockMvc.performOptions(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.options(url, uriVars))
    perform.init()
    return perform.andReturn()
}

fun MockMvc.performOptions(uri: URI, init: ResultActions.() -> Unit): MvcResult {
    val perform = this.perform(MockMvcRequestBuilders.options(uri))
    perform.init()
    return perform.andReturn()
}