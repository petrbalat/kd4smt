package cz.petrbalat.spring.mvc.test.dsl

import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.JsonPathResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


fun ResultActionsDsl.printRequestAndResponse(): ResultActionsDsl {
    return this.andDo { print() }
}

fun MockMvcResultMatchersDsl.json(block: JsonResultMatchersDsl.() -> Unit): MockMvcResultMatchersDsl {
    JsonResultMatchersDsl(this).block()
    return this
}

fun MockMvcResultMatchersDsl.json(jsonContent: String, strict: Boolean = false): MockMvcResultMatchersDsl {
    this.content { MockMvcResultMatchers.content().json(jsonContent, strict) }
    return this
}

fun MockMvcResultMatchersDsl.and(matcher: ResultMatcher) {
    this.match(matcher)
}

fun <T> MockMvcResultMatchersDsl.withModel(name: String, modelInit: T.() -> Unit) {
    val resultMatcher: ResultMatcher = ResultMatcher { mvcResult ->
        val model = mvcResult.modelAndView?.model?.get(name) as T?
        model?.modelInit() ?: throw AssertionError("Model attribute $name was not found")
    }
    this.match(resultMatcher)
}


class JsonResultMatchersDsl(val matcher: MockMvcResultMatchersDsl) {

    infix fun String.pathIs(other: Any) {
        matcher.jsonPath(this, CoreMatchers.`is`(other))
    }

    infix fun String.path(block: JsonPathResultMatchers.() -> ResultMatcher) {
        matcher.match(MockMvcResultMatchers.jsonPath(this).block())
    }

    infix fun <T> String.pathMatcher(value: Matcher<T>) {
        matcher.jsonPath(this, value)
    }

}