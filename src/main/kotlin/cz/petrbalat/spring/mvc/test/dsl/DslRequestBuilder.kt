package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
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

    fun expectStatus(statusInit: StatusResultMatchers.() -> ResultMatcher) {
        expect { status(statusInit) }
    }

    fun expectContent(contentInit: ContentResultMatchers.() -> ResultMatcher) {
        expect { content(contentInit) }
    }

    fun expectViewName(viewName: String) {
        return expect {viewName(viewName)}
    }

    fun expectModel(modelInit: ModelResultMatchers.() -> ResultMatcher) {
        expect { model(modelInit) }
    }

    fun <T> expectModel(name: String, modelInit: T.() -> Unit) {
        expect { model<T>(name, modelInit) }
    }

    fun expectRedirectedUrl(expectedUrl: String) {
        expect { redirectedUrl(expectedUrl)}
    }

    fun expectRedirectedUrlPattern(redirectedUrlPattern: String) {
        expect { redirectedUrlPattern(redirectedUrlPattern) }
    }

    fun expectHeader(headerInit: HeaderResultMatchers.() -> ResultMatcher) {
        expect { header(headerInit) }
    }

    fun expectFlash(flashInit: FlashAttributeResultMatchers.() -> ResultMatcher) {
        expect { flash(flashInit) }
    }

    fun expectJsonPath(expression:String, vararg args:Any, jsonInit: JsonPathResultMatchers.() -> ResultMatcher) {
        expect { jsonPath(expression, args = *arrayOf(args), jsonInit = jsonInit) }
    }

    fun expectXPath(expression:String, vararg args:Any, xpatInit: XpathResultMatchers.() -> ResultMatcher) {
        expect { xPath(expression, args = *arrayOf(args), xpatInit = xpatInit) }
    }

    fun expectCookie(cookieInit: CookieResultMatchers.() -> ResultMatcher) {
        expect { cookie(cookieInit) }
    }
}