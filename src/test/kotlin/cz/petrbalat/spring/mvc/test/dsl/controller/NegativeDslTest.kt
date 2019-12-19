package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.*
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
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
import org.xml.sax.SAXParseException
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [KD4SMTApplication::class])
class NegativeDslTest {

    @Autowired
    lateinit var context: WebApplicationContext

    val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `hello json`() {
        val name = "Petr"
        mockMvc.performGet("/hello/$name") {
            printRequestAndResponse()
            expect {
                assertThrows<AssertionError> { content { contentType(MediaType.APPLICATION_ATOM_XML) } }
                assertThrows<AssertionError> { contentString("Wrong") }
                assertThrows<AssertionError> { jsonPath("surname", `is`("Wrong")) }
                assertThrows<AssertionError> { json("""{"name":"wrong"}""") }
                assertThrows<AssertionError> { jsonPath("surname") { value("wrong") } }
                assertThrows<AssertionError> { cookie { value("name", "wrong") } }
                assertThrows<AssertionError> { flash { attribute("name", "wrong") } }
                assertThrows<AssertionError> { header { stringValues("name", "wrong") } }
                assertThrows<AssertionError> { model { attributeExists("name", "wrong") } }
                assertThrows<AssertionError> { model<String>("name") { assertThat(this, `is`("wrong")) } }
                assertThrows<AssertionError> { redirectedUrl("wrong/Url") }
                assertThrows<AssertionError> { redirectedUrlPattern("wrong/Url") }
                assertThrows<AssertionError> { redirectedUrlPattern("wrong/Url") }
                assertThrows<AssertionError> { status { isAccepted } }
                assertThrows<AssertionError> { viewName("wrongName") }
                assertThrows<AssertionError> { ACCEPTED.isStatus() }
                assertThrows<AssertionError> { "$.surname" jsonPathIs "wrong" }
                assertThrows<AssertionError> { "$.surname" jsonPathMatcher `is`("wrong") }
                assertThrows<AssertionError> { "surname" jsonPath { value("wrong") } }
                assertThrows<AssertionError> { jsonPath("surname") { value("wrong") } }
                assertThrows<AssertionError> { +HandlerMethod("helloJsonWrong") }
                assertThrows<SAXParseException> { xpath("//h1") { nodeCount(1) } }
            }
        }
    }

    @Test
    fun `hello html`() {
        mockMvc.performGet("/hello") {
            builder {
                param("name", "Petr")
            }
            expect {
                assertThrows<AssertionError> { xpath("//wrong") { nodeCount(1) } }
            }
        }
    }
}


