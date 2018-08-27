package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.*
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.AssertionErrors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [KD4SMTApplication::class])
class DslControllerTest  {

    @Autowired
    lateinit var context: WebApplicationContext

    val mockMvc: MockMvc  by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `hello json`() {
        val name = "Petr"
        mockMvc.performGet("/hello/$name") {
            printRequestAndResponse() //Autocomplete that enables `print()` action
            expect {
                json("""{"surname":"Petr"}""", strict = false)  //JsonAssert support (non-strict is the default)
                "$.surname" jsonPathIs name //JsonPath

                "surname" jsonPath { value("Petr")}
                jsonPath("surname") {value("Petr")}

                +HandlerMethod("helloJson")
            }
        }
    }

    @Test
    fun helloGet() {
        mockMvc.performGet("/hello?name=Petr") {
            andDo(print())
                    .andExpect(status().isOk) //legacy andDo/andExpect can still be used
                    .expectStatus { isOk } //chaining is supported but not really necessary
            expectContent { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            expectViewName("hello")

            expectModel {
                size<Any>(1)
                attribute("name", "Petr")
            }

            expectXPath("//h1") {
                nodeCount(1)
            }

            expectXPath("""//span[@class="name"]""") {
                nodeCount(1)
                string("Petr")
            }

            actions {
                assertTrue(andReturn().response.contentAsString.contains("Hello world"))
            }

            withResult {
                assertTrue(response.contentAsString.contains("Hello world"))
            }
        }
    }

    @Test
    fun `test get page`() = mockMvc.performGet(createUri("/hello?name={0}", "Petr")) {
        expectStatus { isOk }
        expectContent { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
        expectViewName("hello")

        expectModel {
            size<Any>(1)
            attribute("name", "Petr")
        }

        expectXPath("//h1") {
            nodeCount(1)
        }

        expectXPath("""//span[@class="name"]""") {
            nodeCount(1)
            string("Petr")
        }
    }

    @Test
    fun `test post page`() = mockMvc.performPost("/hello") {
        builder {
            contentType(MediaType.APPLICATION_FORM_URLENCODED)
            param("surname", "Balat")
        }

        expectModel<HelloPostDto>("helloPostDto") {
            assertEquals("Balat", surname)
        }

        expectXPath("//h1") {
            nodeCount(1)
        }

        expectXPath("""//span[@class="name"]""") {
            nodeCount(1)
            string("Balat")
        }
    }

    @Test
    fun `hello put with required parameters of method and url`() {
        mockMvc.performPut("/hello") {
            builder {
                contentType(MediaType.APPLICATION_JSON)
                content("""{"surname": "Jack"}""")
                cookie(Cookie("cookieName", "Extra Things"))
            }
            printRequestAndResponse()

            expect {
                status { isBadRequest }
                json("""{"surname":"Jack"}""")
                json("""{"surname":"Jack", "extraName":"Extra Things"}""", strict = true)
            }
            expect { "$.surname" jsonPathIs "Jack" } //builder,actions, and expects can be called multiple times
        }
    }

    @Test
    fun `minimal call, builder, and expectation`() = mockMvc.performGet( "/hello") {
        builder { param("name", "world") }
        expect { status { isOk } }
    }
}

/** Silly example matcher to demonstrate how to add custom matchers that aren't in the DSL using unary operator */
class HandlerMethod(private val name: String): ResultMatcher {

    override fun match(result: MvcResult) {
        val handler = result.handler
        if(handler is org.springframework.web.method.HandlerMethod) {
            AssertionErrors.assertEquals("Handler name", name, handler.method.name)
        }
    }
}
