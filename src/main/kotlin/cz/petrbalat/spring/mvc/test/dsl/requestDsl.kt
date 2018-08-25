package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders




fun MockHttpServletRequestBuilder.jsonContent(jsonContent: String) {
    content(jsonContent)
    contentType(MediaType.APPLICATION_JSON)
    accept(MediaType.APPLICATION_JSON)
}