package cz.petrbalat.spring.mvc.test.dsl

import org.hamcrest.CoreMatchers.`is`
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.*


@DslMarker
annotation class RequestDsl

@DslMarker
annotation class ResultDsl

fun MockMvc.request(method: HttpMethod, url: String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    val request = DslRequestBuilder(MockMvcRequestBuilders.request(method, url)).apply(block)
    val result = this.perform(request.buildRequest())
    return request.applyResult(result).andReturn()
}

@RequestDsl
class DslRequestBuilder(private val requestBuilder: MockHttpServletRequestBuilder,
                        private val requestBuilders: MutableList<MockHttpServletRequestBuilder.() -> Unit> = mutableListOf(),
                        private val actions: MutableList<ResultActions.() -> Unit> = mutableListOf(),
                        private val expects: MutableList<DslExpectationBuilder.() -> Unit> = mutableListOf()) {

    fun printRequestAndResponse() {
        actions { andDo(MockMvcResultHandlers.print()) }
    }

    fun builder(block: MockHttpServletRequestBuilder.() -> Unit) {
        requestBuilders.add(block)
    }

    fun expect(block: DslExpectationBuilder.() -> Unit) {
        this.expects.add(block)
    }

    fun actions(block: ResultActions.() -> Unit) {
        this.actions.add(block)
    }

    fun buildRequest(): RequestBuilder {
        requestBuilders.forEach { requestBuilder.apply(it) }
        return requestBuilder
    }

    fun applyResult(result: ResultActions): ResultActions {
        actions.forEach { result.apply(it) }
        val expectationBuild = DslExpectationBuilder(result)
        expects.forEach { expectationBuild.apply(it) }
        return result
    }
}

@ResultDsl
class DslExpectationBuilder(private val actions: ResultActions) {

    fun status(statusInit: StatusResultMatchers.() -> ResultMatcher) {
        val status = MockMvcResultMatchers.status().statusInit()
        actions.andExpect(status)
    }

    fun content(contentInit: ContentResultMatchers.() -> ResultMatcher) {
        val content = MockMvcResultMatchers.content().contentInit()
        actions.andExpect(content)
    }

    fun viewName(viewName: String) {
        val view = MockMvcResultMatchers.view().name(viewName)
        actions.andExpect(view)
    }

    fun model(modelInit: ModelResultMatchers.() -> ResultMatcher) {
        val model = MockMvcResultMatchers.model().modelInit()
        actions.andExpect(model)
    }

    fun <T> model(name: String, modelInit: T.() -> Unit) {
        actions.andExpect { mvcResult ->
            val model = mvcResult.modelAndView?.model?.get(name) as T?
            model?.modelInit()
        }
    }

    fun redirectedUrl(expectedUrl: String) {
        val header = MockMvcResultMatchers.redirectedUrl(expectedUrl)
        actions.andExpect(header)
    }

    fun redirectedUrlPattern(redirectedUrlPattern: String) {
        val header = MockMvcResultMatchers.redirectedUrl(redirectedUrlPattern)
        actions.andExpect(header)
    }

    fun header(headerInit: HeaderResultMatchers.() -> ResultMatcher) {
        val header = MockMvcResultMatchers.header().headerInit()
        actions.andExpect(header)
    }

    fun flash(flashInit: FlashAttributeResultMatchers.() -> ResultMatcher) {
        val flash = MockMvcResultMatchers.flash().flashInit()
        actions.andExpect(flash)
    }

    fun jsonPath(expression:String, vararg args:Any, jsonInit: JsonPathResultMatchers.() -> ResultMatcher) {
        val json = MockMvcResultMatchers.jsonPath(expression, args).jsonInit()
        actions.andExpect(json)
    }

    fun xPath(expression:String, vararg args:Any, xpatInit: XpathResultMatchers.() -> ResultMatcher) {
        val xpath = MockMvcResultMatchers.xpath(expression, args).xpatInit()
        actions.andExpect(xpath)
    }

    fun cookie(cookieInit: CookieResultMatchers.() -> ResultMatcher) {
        val cookie = MockMvcResultMatchers.cookie().cookieInit()
        actions.andExpect(cookie)
    }

    fun HttpStatus.isStatus() {
        status { `is`(this@isStatus.value()) }
    }

    fun contentString(value: String){
        content { string(value) }
    }

    infix fun String.jsonPathIs(value: Any) {
        actions.andExpect(MockMvcResultMatchers.jsonPath(this, `is`(value)))
    }
}

fun MockHttpServletRequestBuilder.jsonContent(jsonContent: String) {
    content(jsonContent)
    contentType(MediaType.APPLICATION_JSON)
    accept(MediaType.APPLICATION_JSON)
}