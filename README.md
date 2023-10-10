# Practice SpringBoot & Redis & SwaggerUi

* SpringBoot Restful API & Connect Redis DB CRUD. 
>>Connect Redis   
```java
public static Jedis jedis = new Jedis("localhost");
public static void main(String[] args) {
  try {
        // If Redis setting password, Need use auth
        // jedis.auth("123456"); 
        System.out.println("Connection success");
        System.out.println("Service running: "+jedis.ping());
    SpringApplication.run(DemoApplication.class, args);
  }catch(Exception e) {
    System.out.print(e.toString());
  }
}
```

>>Model  
```java
public class RedisModel {
  public RedisModel(String key, String value) {
    super();
    this.key = key;
    this.value = value;
  }
  private String key;
  private String value;
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
}
```
  
>>GetMapping  
```java
@GetMapping("/{key}")
public Map<String, Object> getOneData(@PathVariable("key") String key) {
  map.clear();
  if (DemoApplication.jedis.get(key)==null) {
    map.put("msg", "no data");
    return map;
  }else {
    map.put(key, DemoApplication.jedis.get(key));
        return map;
  }
}
```
```java
@GetMapping("/all")
public Map<String, Object> getAllData(@RequestParam Map<String, String> allParams) {
  Set<String> keys = DemoApplication.jedis.keys("*"); 
      Iterator<String> k=keys.iterator();
      while(k.hasNext()){   
          String key = k.next();   
          map.put(key, DemoApplication.jedis.get(key).replaceAll("\r\n", ""));
      }
  return map;
}
```

![image](demo_img/get.png?raw=true).  
![image](demo_img/get_db.png?raw=true).  
  
>>PostMapping  
```java
@PostMapping("/data")
public ResponseEntity<Map<String, Object>> createRedisData(@RequestBody RedisModel request) {
  map.clear();
    DemoApplication.jedis.set(request.getKey(),request.getValue());
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(request.getKey())
            .toUri();
  map.put("msg", "success");
  map.put(request.getKey(), request.getValue());
    return ResponseEntity.created(location).body(map);
}
```
![image](demo_img/post_.png?raw=true).  
![image](demo_img/post_db.png?raw=true).  
  
>>PutMapping  
```java
@PutMapping("/{key}")
public Map<String, Object> updateRedisData(
  @PathVariable("key") String key, @RequestBody RedisModel request) {
  map.clear();
  if (DemoApplication.jedis.get(key)==null) {
    map.put("msg", "no data");
    return map;
  }else {
    DemoApplication.jedis.set(key, request.getValue());
    map.put("msg", "success update");
    map.put(key, DemoApplication.jedis.get(key));
        return map;
  }
}
```

![image](demo_img/put.png?raw=true).  
![image](demo_img/put_db.png?raw=true).  
  
>>DeleteMapping  
```java
@DeleteMapping("/{key}")
public Map<String, Object> deleteProduct(@PathVariable("key") String key) {
  map.clear();
  if (DemoApplication.jedis.get(key)==null) {
    map.put("msg", "no data");
    return map;
  }else {
    DemoApplication.jedis.del(key);
    map.put("msg", "success del");
        return map;
  }
}
```

![image](demo_img/del.png?raw=true).  
![image](demo_img/del_db.png?raw=true).  


## SwaggerUi  
To add a package to the pom.xml file
```java
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>2.1.4</version>
</dependency>
```
```java
@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/3.45.0/");
    }
}
```
