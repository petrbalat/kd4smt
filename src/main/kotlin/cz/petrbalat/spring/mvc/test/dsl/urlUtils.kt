package cz.petrbalat.spring.mvc.test.dsl

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 *
 * @author balat
 */

fun createUri(url: String, vararg uriVariableValues:Any?): URI =
        UriComponentsBuilder.fromUriString(url).buildAndExpand(*uriVariableValues).encode().toUri()