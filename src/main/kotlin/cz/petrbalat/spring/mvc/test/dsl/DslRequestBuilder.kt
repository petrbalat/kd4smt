package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.*

@DslMarker
annotation class RequestDsl

@DslMarker
annotation class ResultDsl

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

    fun expectStatus(statusInit: StatusResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { status(statusInit) }
        return this
    }

    fun expectContent(contentInit: ContentResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { content(contentInit) }
        return this
    }

    fun expectViewName(viewName: String): DslRequestBuilder {
        expect {viewName(viewName)}
        return this
    }

    fun expectModel(modelInit: ModelResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { model(modelInit) }
        return this
    }

    fun <T> expectModel(name: String, modelInit: T.() -> Unit): DslRequestBuilder {
        expect { model(name, modelInit) }
        return this
    }

    fun expectRedirectedUrl(expectedUrl: String): DslRequestBuilder {
        expect { redirectedUrl(expectedUrl)}
        return this
    }

    fun expectRedirectedUrlPattern(redirectedUrlPattern: String): DslRequestBuilder {
        expect { redirectedUrlPattern(redirectedUrlPattern) }
        return this
    }

    fun expectHeader(headerInit: HeaderResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { header(headerInit) }
        return this
    }

    fun expectFlash(flashInit: FlashAttributeResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { flash(flashInit) }
        return this
    }

    fun expectJsonPath(expression:String, vararg args:Any, jsonInit: JsonPathResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { jsonPath(expression, args = *arrayOf(args), jsonInit = jsonInit) }
        return this
    }

    fun expectXPath(expression:String, vararg args:Any, xpatInit: XpathResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { xPath(expression, args = *arrayOf(args), xpatInit = xpatInit) }
        return this
    }

    fun expectCookie(cookieInit: CookieResultMatchers.() -> ResultMatcher): DslRequestBuilder {
        expect { cookie(cookieInit) }
        return this
    }

    fun andDo(action: ResultHandler): DslRequestBuilder {
        actions { andDo(action)}
        return this
    }

    fun andExpect(action: ResultMatcher): DslRequestBuilder {
        actions { andExpect(action)}
        return this
    }

    fun withResult(block: MvcResult.() -> Unit) {
        actions { andReturn().apply(block) }
    }
}