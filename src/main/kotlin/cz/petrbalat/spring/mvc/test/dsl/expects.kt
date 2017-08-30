package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.*

fun ResultActions.expectStatus(statusInit: StatusResultMatchers.() -> ResultMatcher) {
    val status = MockMvcResultMatchers.status().statusInit()
    this.andExpect(status)
}

fun ResultActions.expectContent(contentInit: ContentResultMatchers.() -> ResultMatcher) {
    val content = MockMvcResultMatchers.content().contentInit()
    this.andExpect(content)
}

fun ResultActions.expectViewName(viewName: String): ResultActions {
    val view = MockMvcResultMatchers.view()
    return this.andExpect(view.name(viewName))
}

fun ResultActions.expectModel(modelInit: ModelResultMatchers.() -> ResultMatcher): ResultActions {
    val model = MockMvcResultMatchers.model().modelInit()
    return this.andExpect(model)
}

fun <T> ResultActions.expectModel(name: String, modelInit: T.() -> Unit): ResultActions {
    return this.andExpect({ mvcResult ->
        val model = mvcResult.modelAndView.model[name] as T?
        model?.modelInit()
    })
}

fun ResultActions.expectHeader(headerInit: HeaderResultMatchers.() -> ResultMatcher): ResultActions {
    val header = MockMvcResultMatchers.header().headerInit()
    return this.andExpect(header)
}

fun ResultActions.expectFlash(flashInit: FlashAttributeResultMatchers.() -> ResultMatcher): ResultActions {
    val flash = MockMvcResultMatchers.flash().flashInit()
    return this.andExpect(flash)
}

fun ResultActions.expectJsonPath(expression:String, vararg args:Any, jsonInit: JsonPathResultMatchers.() -> ResultMatcher): ResultActions {
    val json = MockMvcResultMatchers.jsonPath(expression, args).jsonInit()
    return this.andExpect(json)
}

fun ResultActions.expectXPath(expression:String, vararg args:Any, xpatInit: XpathResultMatchers.() -> ResultMatcher): ResultActions {
    val xpath = MockMvcResultMatchers.xpath(expression, args).xpatInit()
    return this.andExpect(xpath)
}

fun ResultActions.expectCookie(cookieInit: CookieResultMatchers.() -> ResultMatcher): ResultActions {
    val cokie = MockMvcResultMatchers.cookie().cookieInit()
    return this.andExpect(cokie)
}