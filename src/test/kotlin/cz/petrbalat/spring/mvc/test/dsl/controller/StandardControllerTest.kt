package cz.petrbalat.spring.mvc.test.dsl.controller

import cz.petrbalat.spring.mvc.test.dsl.KD4SMTApplication
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.ModelAndView

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [KD4SMTApplication::class])
class StandardControllerTest {

    @Autowired
    lateinit var context: WebApplicationContext

    val mvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun hello() {
        val requestBuilder = get("/hello?name={1}", "Petr")
        val className = MockMvcResultMatchers.xpath("""//span[@class="name"]""")
        val actions: ResultActions = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.xpath("//h1").nodeCount(1))
                .andExpect(className.nodeCount(1))
                .andExpect(className.string("Petr"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        val result: MvcResult = actions.andReturn()

        val modelAndView: ModelAndView = result.modelAndView!!
        assertEquals("hello", modelAndView.viewName)
        assertEquals(1, modelAndView.model.size)
        assertEquals("Petr", modelAndView.model["name"])
    }

    @Test
    fun helloPost() {
        val contentType = post("/hello").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("surname", "Balat")
        val className = MockMvcResultMatchers.xpath("""//span[@class="name"]""")
        val actions: ResultActions = mvc.perform(contentType)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(className.nodeCount(1))
                .andExpect(className.string("Balat"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        val result: MvcResult = actions.andReturn()

        val modelAndView: ModelAndView = result.modelAndView!!
        assertEquals("hello", modelAndView.viewName)
        assertEquals(2, modelAndView.model.size)//helloPostDto and bindingResult
        assertEquals("Balat", (modelAndView.model["helloPostDto"] as HelloPostDto).surname)
    }
}