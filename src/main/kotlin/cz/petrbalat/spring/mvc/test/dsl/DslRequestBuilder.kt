package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

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