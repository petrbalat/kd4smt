# kd4smt
Kotlin dsl for spring mvc test

standart spring mvc test (see StandardControllerTest):

```kotlin
 val mvc: MockMvc
 
 @Test
 fun hello() {
         val requestBuilder = get("/hello?name={1}", "Petr")
         val actions: ResultActions = mvc.perform(requestBuilder)
                 .andExpect(MockMvcResultMatchers.status().isOk)
                 .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
         val result: MvcResult = actions.andReturn()
 
         val modelAndView: ModelAndView = result.modelAndView
         assertEquals("hello", modelAndView.viewName)
         assertEquals(1, modelAndView.model.size)
         assertEquals("Petr", modelAndView.model["name"])
 
         val response = result.response.contentAsString
         assertTrue(response.contains("Hello"))
         assertTrue(response.contains("Petr"))
     }

```


dsl spring mvc test (see DslControllerTest):
```kotlin
 val mvc: MockMvc
 
 @Test
 fun helloGet() {
         mockMvc.performGet("/hello?name=Petr") {
             expectStatus {
                 isOk
             }
 
             expectContent {
                 contentTypeCompatibleWith(MediaType.TEXT_HTML)
             }
 
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
         expectStatus {
             isOk
         }
 
         expectContent {
             contentTypeCompatibleWith(MediaType.TEXT_HTML)
         }
 
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
```

Gradle:
```
repositories {
    url("https://mymavenrepo.com/repo/plFabfZlZhQO7UjsJKDc/")
}
    
...

testCompile "cz.petrbalat:kd4smt:0.1.0"
```

Maven:
```xml
<dependency>
    <groupId>cz.petrbalat</groupId>
    <artifactId>kd4smt</artifactId>
    <version>0.1.0</version>
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