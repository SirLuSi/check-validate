# check-validate
极好用的基于springBoot验证框架
* 本框架基于springBoot-2.1.5.RELEASE开发

## 最佳实践
### 1.编译代码生成check-validate-1.0-SNAPSHOT.jar
### 2.上传自己的maven私服
### 3.在项目pom文件引入
```xml
        <dependency>
            <groupId>com.lusi.framework.</groupId>
            <artifactId>check-validate</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
### 4.Application中开启验证
```java
@SpringBootApplication
@EnableValidate
public class ValidDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValidDemoApplication.class, args);
    }

}
```

## 例一
```java
    @CheckValid
    @RequestMapping("hello")
    public String hello(@NotBlank(message = "姓名不能为空") String name) {

        return "hello";
    }
```
## 例二
```java
    @RequestMapping("hello")
    public String hello(@CheckValid User user) {

        return "hello";
    }
```
## 想要验证不通过后的自定义响应？很简单..
### 继承AbstractReturnDefinition例如:
```
@Component
public class MyReturnDefinition extends AbstractReturnDefinition {
    @Override
    public Object _return(Set<? extends ConstraintViolation<?>> set, Class retrnType) {
        String message = set.stream().findFirst().get().getMessage();
        if (retrnType.isAssignableFrom(ReturnInfo.class)) {
            return new ReturnInfo(message);
        } else if (retrnType.isAssignableFrom(ApiResponse.class)) {
            return new ApiResponse(message);
        }
        throw new RuntimeException(message);
    }
}
```
