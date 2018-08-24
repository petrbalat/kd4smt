package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.*
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.StatusResultMatchers
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@WebMvcTest(HelloController::class)
@ActiveProfiles("test")
class DslControllerTestV2Test {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun helloGet() {
        mockMvc.request { PUT("/hello") {
                builder {
                    contentType(MediaType.APPLICATION_JSON)
                    content("""{"surname": "Jack"}""")
                    cookie(Cookie("cookieName", "Extra Things"))
                }
                printRequestAndResponse()

                expect {
                    status { isBadRequest }
                    "$.surname" jsonPath "Jack"
                }

                actions {
                    andDo(print())
                    expectContent { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                }
            }
        }


        mockMvc.request(PUT, "/hello") {
            builder {
                contentType(MediaType.APPLICATION_JSON)
                content("""{"surname": "Jack"}""")
                cookie(Cookie("cookieName", "Extra Things"))
            }
            printRequestAndResponse()

            expect {
                status { isBadRequest }
                "$.surname" jsonPath "Jack"
            }
            expect { status {isOk}} //builder,actions, and expects can be called multiple times
        }
    }


}

@DslMarker
annotation class RequestDsl

@DslMarker
annotation class ResultDsl

fun MockMvc.request(method:HttpMethod, url:String, block: DslRequestBuilder.() -> Unit = {}): MvcResult {
    val request = DslRequestBuilder(url,MockMvcRequestBuilders.request(method, url)).apply(block)
    val result = this.perform(request.buildRequest())
    return request.applyResult(result).andReturn()
}

fun MockMvc.request(block: MockMvcDslBuilder.() -> Unit = {}): MvcResult {
    val request = MockMvcDslBuilder().apply(block).requestBuilder
    val result = this.perform(request.buildRequest())
    return request.applyResult(result).andReturn()
}


// ===== Root Builders ===== //
@RequestDsl
class MockMvcDslBuilder(internal var url: String? = null,
                        internal var requestBuilder: DslRequestBuilder = DslRequestBuilder(url)) {
    operator fun HttpMethod.invoke(url: String? = null, block: DslRequestBuilder.() -> Unit) {
        val req = requestBuilder.apply(block)
        req.requestBuilder = MockMvcRequestBuilders.request(this, url ?: req.url ?: throw IllegalArgumentException("Url wasn't set"))

    }
}

@RequestDsl
class DslRequestBuilder(internal var url: String? = null,
                        internal var requestBuilder: MockHttpServletRequestBuilder? = null,
                        internal var requestBuilders: List<MockHttpServletRequestBuilder.() -> Unit> = listOf(),
                        internal var rawActions: List<ResultActions.() -> Unit> = listOf(),
                        internal var expects: List<DslExpectationBuilder.() -> Unit> = listOf()) {

    fun printRequestAndResponse() {
        actions { andDo(print()) }
    }

    fun request(block: MockHttpServletRequestBuilder.() -> Unit) {
        requestBuilders + block
    }

    fun builder(block: MockHttpServletRequestBuilder.() -> Unit) {
        requestBuilders + block
    }

    fun expect(block: DslExpectationBuilder.() -> Unit) {
        this.expects + block
    }

    fun actions(block: ResultActions.() -> Unit) {
        this.rawActions + block
    }

    fun applyResult(result: ResultActions): ResultActions {
        rawActions.forEach { result.apply(it) }
        val expectationBuild = DslExpectationBuilder(result)
        expects.forEach { expectationBuild.apply(it) }
        return result
    }

    fun buildRequest(): RequestBuilder {
        if(requestBuilder != null) {
            requestBuilders.forEach { requestBuilder?.apply(it) }
        }
      return requestBuilder ?: throw IllegalArgumentException("Builder wasn't created (report potential DSL bug)")
    }
}

@ResultDsl
class DslExpectationBuilder(val actions: ResultActions) {

    fun status(statusInit: StatusResultMatchers.() -> ResultMatcher){
        val status = MockMvcResultMatchers.status().statusInit()
        actions.andExpect(status)
    }

    infix fun String.jsonPath(value: String) {
        actions.andExpect(MockMvcResultMatchers.jsonPath(this, `is`(value)))
    }

}