# kd4smt - Kotlin dsl for spring mvc test

Imagine that you have spring mvc controller
```kotlin
@Controller
class HelloController {

    @GetMapping("/hello")
    fun hello(@RequestParam name: String, modelMap: ModelMap): String {
        modelMap["name"] = name
        return "hello"
    }
    
    @GetMapping("/hello/{name}")
    @ResponseBody
    fun helloJson(@PathVariable name: String) = HelloPostDto(surname = name)


    @PostMapping("/hello")
    fun helloPost(dto: HelloPostDto, modelMap: ModelMap): String = "hello"

}


data class HelloPostDto(var surname:String = "World")
```


### Standart spring mvc test (see [StandardControllerTest](src/test/kotlin/cz/petrbalat/spring/mvc/test/dsl/controller/StandardControllerTest.kt)): 

```kotlin
 val mvc: MockMvc
 
 
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
 
         val modelAndView: ModelAndView = result.modelAndView
         assertEquals("hello", modelAndView.viewName)
         assertEquals(1, modelAndView.model.size)
         assertEquals("Petr", modelAndView.model["name"])
     }

```

### With this library dsl spring mvc test (see [DslControllerTest](src/test/kotlin/cz/petrbalat/spring/mvc/test/dsl/controller/DslControllerTest.kt)):
```kotlin
val mockMvc: MockMvc

@Test
fun `hello json`() {
    val name = "Petr"
    mockMvc.performGet("/hello/$name") {
        printRequestAndResponse() //Autocomplete that enables `print()` action
        expect {
            json("""{"surname":"Petr"}""", strict = false)  //JsonAssert support (non-strict is the default)
            "$.surname" jsonPathIs name //JsonPath 
        }
    }
}

@Test
fun helloGet() {
    mockMvc.performGet("/hello") {
        builder {
            param("name","Petr")
        }
        expect {
            status { isOk }
            content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
            viewName("hello")
            
            model {
                size<Any>(1)
                attribute("name", "Petr")
            }
            xPath("//h1") { nodeCount(1) }
            xPath("""//span[@class="name"]""") {
                nodeCount(1)
                string("Petr")
            }
        }
    }
}
```
## How to use

Gradle:
```
repositories {
    url("https://mymavenrepo.com/repo/plFabfZlZhQO7UjsJKDc/")
}
    
...

testCompile "cz.petrbalat:kd4smt:0.5.0"
```

Maven:
```xml
<dependency>
    <groupId>cz.petrbalat</groupId>
    <artifactId>kd4smt</artifactId>
    <version>0.5.0</version>
    <scope>test</scope>
</dependency>


...

<repository>
    <id>kd4smt-mvn-repo</id>
    <url>https://mymavenrepo.com/repo/plFabfZlZhQO7UjsJKDc/</url>
    <snapshots>
        <enabled>false</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>
```
