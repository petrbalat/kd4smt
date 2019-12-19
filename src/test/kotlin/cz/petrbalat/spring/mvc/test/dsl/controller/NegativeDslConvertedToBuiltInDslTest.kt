package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.KD4SMTApplication
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.xml.sax.SAXParseException

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(classes = [KD4SMTApplication::class])
class NegativeDslConvertedToBuiltInDslTest {

    @Autowired
    lateinit var context: WebApplicationContext

    val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `hello json`() {
        val name = "Petr"
        mockMvc.get("/hello/$name")
                .printRequestAndResponse()
                .andExpect {
                    assertThrows<AssertionError> { content { contentType(MediaType.APPLICATION_ATOM_XML) } }
                    assertThrows<AssertionError> { content { string("Wrong") } }
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
                    assertThrows<AssertionError> { view { name("wrongName") } }
//                    assertThrows<AssertionError> { ACCEPTED.isStatus() } No longer supported
                    assertThrows<AssertionError> { json { "$.surname" pathIs "wrong" } }
                    assertThrows<AssertionError> { json { "$.surname" pathMatcher `is`("wrong") } }
                    assertThrows<AssertionError> { json { "surname" path { value("wrong") } } }
                    assertThrows<AssertionError> { jsonPath("surname") { value("wrong") } }
                    assertThrows<AssertionError> { and(HandlerMethod("helloJsonWrong")) }
                    assertThrows<SAXParseException> { xpath("//h1") { nodeCount(1) } }
                }
    }

    @Test
    fun `hello html`() {
        mockMvc.get("/hello") {
            param("name", "Petr")
        }.andExpect {
            assertThrows<AssertionError> { xpath("//wrong") { nodeCount(1) } }
        }
    }
}


