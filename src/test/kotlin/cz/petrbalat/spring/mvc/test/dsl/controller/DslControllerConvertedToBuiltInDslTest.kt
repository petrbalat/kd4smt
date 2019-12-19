package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.KD4SMTApplication
import cz.petrbalat.spring.mvc.test.dsl.and
import cz.petrbalat.spring.mvc.test.dsl.createUri
import cz.petrbalat.spring.mvc.test.dsl.json
import cz.petrbalat.spring.mvc.test.dsl.printRequestAndResponse
import cz.petrbalat.spring.mvc.test.dsl.withModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.JsonPathResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [KD4SMTApplication::class])
class DslControllerConvertedToBuiltInDslTest {

    @Autowired
    lateinit var context: WebApplicationContext

    val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `hello json`() {
        val name = "Petr"
        mockMvc.get("/hello/$name")
                .printRequestAndResponse() //Autocomplete that enables `print()` action
                .andExpect {
                    json("""{"surname":"Petr"}""", strict = false)  //JsonAssert support (non-strict is the default)
                    json {
                        "$.surname" pathIs name //JsonPath
                        "surname" path { value("Petr") }
                    }

                    jsonPath("surname") { value("Petr") }

                    and(HandlerMethod("helloJson"))
                }
    }

    @Test
    fun helloGet() {
        mockMvc.get("/hello?name=Petr")
                .printRequestAndResponse()
                .andExpect {
                    status().isOk //legacy andDo/andExpect can still be used

                    content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
                    view { name("hello") }

                    model {
                        size(1)
                        attribute("name", "Petr")
                    }

                    xpath("//h1") { nodeCount(1) }

                    xpath("""//span[@class="name"]""") {
                        nodeCount(1)
                        string("Petr")
                    }

                    //Actions are private in new DSL, this approach is no long supported
//                    actions {
//                        assertTrue(andReturn().response.contentAsString.contains("Hello world"))
//                    }

                    match(ResultMatcher { mvcResult ->
                        assertTrue(mvcResult.response.contentAsString.contains("Hello world"))
                    })
                }
    }

    @Test
    fun `test get page`() = mockMvc.get(createUri("/hello?name={0}", "Petr")).andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
        view { name("hello") }

        model {
            size(1)
            attribute("name", "Petr")
        }

        xpath("//h1") {
            nodeCount(1)
        }

        xpath("""//span[@class="name"]""") {
            nodeCount(1)
            string("Petr")
        }
    }

    @Test
    fun `test post page`() = mockMvc.post("/hello") {
        contentType = MediaType.APPLICATION_FORM_URLENCODED
        param("surname", "Balat")
    }.andExpect {

        withModel<HelloPostDto>("helloPostDto") {
            assertEquals("Balat", surname)
        }

        xpath("//h1") {
            nodeCount(1)
        }

        xpath("""//span[@class="name"]""") {
            nodeCount(1)
            string("Balat")
        }
    }

    @Test
    fun `hello put with required parameters of method and url`() {
        mockMvc.put("/hello") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"surname": "Jack"}"""
            cookie(Cookie("cookieName", "Extra Things"))
        }.printRequestAndResponse()
                .andExpect {
                    status { isBadRequest }
                    json("""{"surname":"Jack"}""")
                    json("""{"surname":"Jack", "extraName":"Extra Things"}""", strict = true)
                }.andExpect {
                    json {
                        "$.surname" pathIs "Jack"
                    } //builder,actions, and expects can be called multiple times
                }
    }

    @Test
    fun `minimal call, builder, and expectation`() = mockMvc.get("/hello") {
        param("name", "world")
    }.andExpect { status { isOk } }
}