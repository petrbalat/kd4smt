package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [KD4SMTApplication::class])
class DslControllerTest : MockMvcProvider {

    @Autowired
    lateinit var context: WebApplicationContext

    override val mockMvc: MockMvc  by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun helloGet() {
        mockMvc.performGet("/hello?name=Petr") {
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
    }

    @Test
    fun helloGetExpression() = performGet(createUri("/hello?name={0}", "Petr")) {
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
    fun helloPost() = performPost("/hello", {
        contentType(MediaType.APPLICATION_FORM_URLENCODED)
        param("surname", "Balat")
    }) {
        expectStatus { isOk }
        expectContent { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
        expectViewName("hello")

        expectModel {
            size<Any>(2)
            attribute("helloPostDto", HelloPostDto("Balat"))
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
        mockMvc.request(HttpMethod.PUT, "/hello") {
            builder {
                contentType(MediaType.APPLICATION_JSON)
                content("""{"surname": "Jack"}""")
                cookie(Cookie("cookieName", "Extra Things"))
            }
            printRequestAndResponse()

            expect {
                status { isBadRequest }
            }
            expect { "$.surname" jsonPathIs "Jack" } //builder,actions, and expects can be called multiple times
        }
    }

    @Test
    fun `minimal call, builder, and expectation`() {
        mockMvc.request(HttpMethod.GET, "/hello") {
            builder { param("name", "world") }
            expect { status { isOk } }
        }
    }
}