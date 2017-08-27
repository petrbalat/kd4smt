package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import java.net.URI

interface MockMvcProvider {

    val mockMvc: MockMvc get

}

//GET
fun MockMvcProvider.performGet(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performGet(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performGet(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performGet(uri = uri, init = init)
}

//POST
fun MockMvcProvider.performPost(url:String, vararg uriVars:Any,
                        requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                        init: ResultActions.() -> Unit) {
    this.mockMvc.performPost(url = url, uriVars = uriVars, requestInit = requestInit, init = init)
}

fun MockMvcProvider.performPost(uri: URI,
                                requestInit: MockHttpServletRequestBuilder.() -> Unit = {},
                                init: ResultActions.() -> Unit) {
    this.mockMvc.performPost(uri = uri, requestInit = requestInit ,init = init)
}

//PUT
fun MockMvcProvider.performPut(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performPut(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performPut(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performPut(uri = uri, init = init)
}

//DELETE
fun MockMvcProvider.performDelete(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performDelete(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performDelete(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performDelete(uri = uri, init = init)
}

//HEAD
fun MockMvcProvider.performHead(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performHead(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performHead(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performHead(uri = uri, init = init)
}

//PATCH
fun MockMvcProvider.performPatch(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performPatch(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performPatch(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performPatch(uri = uri, init = init)
}

//OPTIONS
fun MockMvcProvider.performOptions(url:String, vararg uriVars:Any, init: ResultActions.() -> Unit) {
    this.mockMvc.performOptions(url = url, uriVars = uriVars, init = init)
}

fun MockMvcProvider.performOptions(uri: URI, init: ResultActions.() -> Unit) {
    this.mockMvc.performOptions(uri = uri, init = init)
}