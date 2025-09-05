# Spring AI Tool 测试项目

这个项目演示了如何在Spring AI中使用`@Tool`注解创建工具，并通过`ChatClient`调用这些工具。

## 功能特性

- ✅ Spring AI 1.0.0-M6 支持
- ✅ `@Tool`注解识别
- ✅ ChatClient工具调用
- ✅ REST API接口
- ✅ Java 17 支持

## 环境要求

- Java 17+
- Maven 3.6+
- OpenAI API Key

## 配置步骤

### 1. 设置API Key

#### 通义千问（推荐）
```bash
export DASHSCOPE_API_KEY=your-dashscope-api-key-here
```

#### OpenAI（备用）
```bash
export OPENAI_API_KEY=your-openai-api-key-here
```

#### 修改配置文件
编辑 `src/main/resources/application.properties`：
```properties
# 通义千问配置
spring.ai.dashscope.api-key=your-dashscope-api-key-here
spring.ai.dashscope.chat.options.model=qwen-turbo

```

### 2. 运行项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

## API接口

项目启动后，可以通过以下接口测试：

### 通义千问接口

#### 1. 基础对话
```bash
curl "http://localhost:8080/api/qwen/chat?message=你好，请介绍一下自己"
```

#### 2. 工具调用
```bash
curl "http://localhost:8080/api/qwen/with-tools?message=现在几点了？"
```

#### 3. 编程助手
```bash
curl "http://localhost:8080/api/qwen/programming?message=请用Java写一个Hello World程序"
```

#### 4. 模型信息
```bash
curl http://localhost:8080/api/qwen/model-info
```

#### 5. 测试工具调用
```bash
curl http://localhost:8080/api/qwen/test-tools
```

### 通用接口

#### 1. 健康检查
```bash
curl http://localhost:8080/api/chat/health
```

#### 2. 直接测试工具
```bash
curl http://localhost:8080/api/chat/test-tool
```

#### 3. 通过AI调用工具
```bash
curl "http://localhost:8080/api/chat/time?message=请告诉我现在的时间"
```

## 工具定义

项目中的`DateTimeTools`类定义了一个工具：

```java
@Component
public class DateTimeTools {
    @Tool(description = "获取用户时区的当前日期和时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
```

## 工具调用流程

1. **工具注册**：Spring AI自动扫描`@Tool`注解的方法
2. **ChatClient配置**：通过`defaultTools("getCurrentDateTime")`指定可用工具
3. **AI调用**：当用户询问时间相关问题时，AI会自动调用`getCurrentDateTime`工具
4. **结果返回**：工具执行结果会作为AI响应的一部分返回给用户

## 测试验证

运行测试验证工具是否正常工作：

```bash
mvn test
```

## 故障排除

### 1. API Key错误
如果看到"OpenAI API key must be set"错误，请确保正确设置了API Key。

### 2. Java版本问题
确保使用Java 17或更高版本：
```bash
java -version
```

### 3. 工具未识别
检查`@Tool`注解的导入是否正确：
```java
import org.springframework.ai.tool.annotation.Tool;
```