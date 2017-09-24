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

    @PostMapping("/hello")
    fun helloPost(dto: HelloPostDto, modelMap: ModelMap): String = "hello"

}

data class HelloPostDto(var surname:String = "World")
```


### Standart spring mvc test (see StandardControllerTest):

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

### With this library dsl spring mvc test (see DslControllerTest):
```kotlin
 val mvc: MockMvc
 
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
```

or if you implement MockMvcProvider  (see DslControllerTest):

```kotlin
 override val mvc: MockMvc
 
 @Test
 fun helloGetExpression() = performGet("/hello?name=Petr") {
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
    fun helloPost() = performPost("/hello", requestInit = {
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
```

##How to use

Gradle:
```
repositories {
    url("https://mymavenrepo.com/repo/plFabfZlZhQO7UjsJKDc/")
}
    
...

testCompile "cz.petrbalat:kd4smt:0.3.2"
```

Maven:
```xml
<dependency>
    <groupId>cz.petrbalat</groupId>
    <artifactId>kd4smt</artifactId>
    <version>0.3.2</version>
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