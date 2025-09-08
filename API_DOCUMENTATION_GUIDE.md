# HelloworldController API 文档

## 概述

本文档为 `HelloworldController` 提供了基于 JSON Schema 格式的完整 API 文档。该文档自动生成，包含接口定义、参数验证、响应格式和使用示例。

## 快速开始

### 1. 获取 API 文档

```bash
# 获取 HelloworldController 的完整 API 文档
curl -X GET "http://localhost:8080/api-docs/helloworld"

# 获取 API 文档索引
curl -X GET "http://localhost:8080/api-docs/index"
```

### 2. 调用 simpleChat 接口

```bash
# 基本调用
curl -X GET "http://localhost:8080/helloworld/simple/chat?query=你好"

# 带中文参数的调用
curl -X GET "http://localhost:8080/helloworld/simple/chat?query=请介绍一下Spring AI"
```

## 接口详情

### simpleChat 接口

**基本信息**
- **方法名**: `simpleChat`
- **HTTP 方法**: `GET`
- **路径**: `/helloworld/simple/chat`
- **描述**: 与 AI 助手进行对话的简单聊天接口

**请求参数**

| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| query | string | 是 | 用户查询内容 | "你好，请介绍一下你自己" |

**请求参数 Schema**
```json
{
  "type": "object",
  "properties": {
    "query": {
      "type": "string",
      "description": "用户查询内容",
      "minLength": 1,
      "maxLength": 1000
    }
  },
  "required": ["query"]
}
```

**响应格式**

| 字段名 | 类型 | 描述 | 示例 |
|--------|------|------|------|
| data | string | AI 聊天响应内容 | "你好！我是一个智能聊天助手，很高兴为您服务！" |
| success | boolean | 请求是否成功 | true |
| timestamp | string | 响应时间戳 | "2024-01-01T12:00:00Z" |

**响应 Schema**
```json
{
  "type": "object",
  "properties": {
    "data": {
      "type": "string",
      "description": "AI 聊天响应内容"
    },
    "success": {
      "type": "boolean",
      "description": "请求是否成功"
    },
    "timestamp": {
      "type": "string",
      "format": "date-time",
      "description": "响应时间戳"
    }
  },
  "required": ["success", "timestamp", "data"]
}
```

## 使用示例

### JavaScript 调用示例

```javascript
// 使用 fetch API
async function chatWithAI(query) {
  try {
    const response = await fetch(`http://localhost:8080/helloworld/simple/chat?query=${encodeURIComponent(query)}`);
    const data = await response.text();
    console.log('AI 响应:', data);
    return data;
  } catch (error) {
    console.error('请求失败:', error);
  }
}

// 调用示例
chatWithAI('你好，请介绍一下你自己');
```

### Java 调用示例

```java
@RestController
public class ChatClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String chatWithAI(String query) {
        String url = "http://localhost:8080/helloworld/simple/chat?query=" + 
                    URLEncoder.encode(query, StandardCharsets.UTF_8);
        return restTemplate.getForObject(url, String.class);
    }
}
```

### Python 调用示例

```python
import requests
import urllib.parse

def chat_with_ai(query):
    url = f"http://localhost:8080/helloworld/simple/chat?query={urllib.parse.quote(query)}"
    response = requests.get(url)
    return response.text

# 调用示例
result = chat_with_ai("你好，请介绍一下你自己")
print(result)
```

## 特殊说明

### 默认行为
- 如果 `query` 参数为空或未提供，系统将使用默认问题："你好，请介绍一下你自己"
- 接口支持上下文记忆，可以在对话中保持上下文

### 配置信息
- **AI 服务**: 阿里云 DashScope
- **默认提示词**: "你是一个博学的智能聊天助手，请根据用户提问回答！"
- **TopP 参数**: 0.7
- **记忆功能**: InMemoryChatMemory
- **日志记录**: SimpleLoggerAdvisor

### 错误处理
- 如果 AI 服务不可用，会返回相应的错误信息
- 网络超时或参数格式错误会返回 HTTP 错误状态码

## 开发工具集成

### 1. Postman 导入
可以将 API 文档导入到 Postman 中进行测试：
1. 复制 `HELLOWORLD_API_DOCUMENTATION.json` 文件内容
2. 在 Postman 中选择 "Import"
3. 粘贴 JSON 内容进行导入

### 2. Swagger UI 集成
虽然当前使用 JSON Schema 格式，但可以轻松转换为 OpenAPI 格式用于 Swagger UI 显示。

### 3. 前端表单生成
可以使用 JSON Schema 自动生成前端表单：
```javascript
// 使用 JSON Schema 生成表单
import { JSONSchemaForm } from '@rjsf/core';

const schema = {
  type: "object",
  properties: {
    query: {
      type: "string",
      title: "查询内容",
      minLength: 1,
      maxLength: 1000
    }
  },
  required: ["query"]
};

<JSONSchemaForm schema={schema} onSubmit={handleSubmit} />
```

## 测试用例

### 单元测试示例
```java
@Test
public void testSimpleChat() {
    // 测试正常查询
    String response = restTemplate.getForObject(
        "/helloworld/simple/chat?query=你好", String.class);
    assertNotNull(response);
    assertFalse(response.isEmpty());
    
    // 测试空查询（应该使用默认问题）
    String defaultResponse = restTemplate.getForObject(
        "/helloworld/simple/chat", String.class);
    assertNotNull(defaultResponse);
}
```

### 集成测试示例
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloworldControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testSimpleChatEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/helloworld/simple/chat?query=测试", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
```

## 总结

本文档提供了 `HelloworldController` 的完整 API 文档，包括：

✅ **完整的接口定义** - 基于 JSON Schema 的标准化文档  
✅ **参数验证规则** - 详细的请求参数验证规范  
✅ **响应格式说明** - 标准化的响应数据结构  
✅ **多语言示例** - JavaScript、Java、Python 调用示例  
✅ **开发工具集成** - Postman、Swagger UI 等工具支持  
✅ **测试用例** - 单元测试和集成测试示例  

该文档可以用于：
- 前端开发参考
- API 测试和验证
- 自动生成客户端代码
- 接口文档维护
- 团队协作开发
