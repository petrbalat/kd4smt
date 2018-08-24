package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.request
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import javax.servlet.http.Cookie

@ExtendWith(SpringExtension::class)
@WebMvcTest(HelloController::class)
@ActiveProfiles("test")
class DslControllerTestV2Test {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `hello put with required parameters of method and url`() {
        mockMvc.request(PUT, "/hello") {
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
        mockMvc.request(GET, "/hello") {
            builder { param("name", "world") }
            expect { status { isOk } }
        }
    }


}
