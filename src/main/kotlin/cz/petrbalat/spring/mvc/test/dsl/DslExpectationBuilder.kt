package cz.petrbalat.spring.mvc.test.dsl

import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.*

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

    fun <T> jsonPath(expression:String, matcher : Matcher<T>) {
        val json = MockMvcResultMatchers.jsonPath(expression, matcher)
        actions.andExpect(json)
    }

    fun jsonPath(expression:String, vararg args:Any, block: JsonPathResultMatchers.() -> ResultMatcher) {
        val xpath = MockMvcResultMatchers.jsonPath(expression, args).block()
        actions.andExpect(xpath)
    }

    fun xPath(expression:String, vararg args:Any, xpathInit: XpathResultMatchers.() -> ResultMatcher) {
        val xpath = MockMvcResultMatchers.xpath(expression, args).xpathInit()
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

    infix fun String.jsonPath(block: JsonPathResultMatchers.() -> ResultMatcher) {
        actions.andExpect(MockMvcResultMatchers.jsonPath(this).block())
    }

    infix fun String.jsonPathIs(value: Any) {
        actions.andExpect(MockMvcResultMatchers.jsonPath(this, CoreMatchers.`is`(value)))
    }

    infix fun <T> String.jsonPathMatcher(value: Matcher<T>) {
        actions.andExpect(MockMvcResultMatchers.jsonPath(this, value))
    }

    operator fun ResultMatcher.unaryPlus(){
        actions.andExpect(this)
    }

    fun json(jsonContent: String, strict: Boolean = false) {
        actions.andExpect(MockMvcResultMatchers.content().json(jsonContent, strict))
    }
}