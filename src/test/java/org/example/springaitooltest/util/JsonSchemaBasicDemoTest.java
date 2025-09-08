package org.example.springaitooltest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON Schema 基础演示测试
 * 展示 JSON Schema 的基本概念和用途
 */
@SpringBootTest
class JsonSchemaBasicDemoTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 演示 1：什么是 JSON Schema
     * JSON Schema 是一个 JSON 文档，用来描述其他 JSON 文档的结构
     */
    @Test
    void demonstrateWhatIsJsonSchema() throws Exception {
        System.out.println("=== JSON Schema 基础概念演示 ===\n");
        
        // 1. 定义一个简单的 JSON Schema
        String schemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "用户信息",
              "description": "用户基本信息的数据结构",
              "properties": {
                "name": {
                  "type": "string",
                  "description": "用户姓名",
                  "minLength": 2,
                  "maxLength": 50
                },
                "age": {
                  "type": "integer",
                  "description": "用户年龄",
                  "minimum": 0,
                  "maximum": 150
                },
                "email": {
                  "type": "string",
                  "description": "邮箱地址",
                  "format": "email"
                }
              },
              "required": ["name", "age"]
            }
            """;
        
        System.out.println("1. JSON Schema 定义（用来描述数据结构的规则）:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(schemaJson)));
        
        // 2. 符合 Schema 的有效数据
        String validDataJson = """
            {
              "name": "张三",
              "age": 25,
              "email": "zhangsan@example.com"
            }
            """;
        
        System.out.println("\n2. 符合 Schema 的有效数据:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(validDataJson)));
        
        // 3. 不符合 Schema 的无效数据
        String invalidDataJson = """
            {
              "name": "李四",
              "age": "25岁",
              "email": "invalid-email"
            }
            """;
        
        System.out.println("\n3. 不符合 Schema 的无效数据（age 应该是数字，email 格式不正确）:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(invalidDataJson)));
        
        System.out.println("\n=== JSON Schema 的作用 ===");
        System.out.println("✅ 定义数据结构：描述 JSON 数据应该包含哪些字段");
        System.out.println("✅ 类型验证：确保字段的数据类型正确");
        System.out.println("✅ 格式验证：验证邮箱、日期等特殊格式");
        System.out.println("✅ 约束检查：检查字符串长度、数字范围等");
        System.out.println("✅ 必填验证：确保必需字段存在");
    }
    
    /**
     * 演示 2：JSON Schema 在 API 开发中的应用
     */
    @Test
    void demonstrateApiUsage() throws Exception {
        System.out.println("\n=== JSON Schema 在 API 开发中的应用 ===\n");
        
        // 1. 定义 API 请求参数的 Schema
        String requestSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "创建用户 API 请求",
              "properties": {
                "username": {
                  "type": "string",
                  "minLength": 3,
                  "maxLength": 20,
                  "pattern": "^[a-zA-Z0-9_]+$"
                },
                "password": {
                  "type": "string",
                  "minLength": 8
                },
                "email": {
                  "type": "string",
                  "format": "email"
                }
              },
              "required": ["username", "password", "email"]
            }
            """;
        
        System.out.println("1. API 请求参数 Schema（定义客户端应该发送什么数据）:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(requestSchemaJson)));
        
        // 2. 定义 API 响应数据的 Schema
        String responseSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "创建用户 API 响应",
              "properties": {
                "success": {
                  "type": "boolean"
                },
                "message": {
                  "type": "string"
                },
                "data": {
                  "type": "object",
                  "properties": {
                    "userId": {
                      "type": "string"
                    },
                    "username": {
                      "type": "string"
                    },
                    "email": {
                      "type": "string"
                    },
                    "createdAt": {
                      "type": "string",
                      "format": "date-time"
                    }
                  }
                }
              },
              "required": ["success", "message"]
            }
            """;
        
        System.out.println("\n2. API 响应数据 Schema（定义服务端返回什么数据）:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(responseSchemaJson)));
        
        System.out.println("\n=== API 开发中的实际应用 ===");
        System.out.println("🔧 前端开发：根据 Schema 生成表单验证规则");
        System.out.println("🔧 后端开发：根据 Schema 验证请求参数");
        System.out.println("🔧 测试：根据 Schema 生成测试数据");
        System.out.println("🔧 文档：根据 Schema 自动生成 API 文档");
    }
    
    /**
     * 演示 3：JSON Schema 的常见验证规则
     */
    @Test
    void demonstrateValidationRules() throws Exception {
        System.out.println("\n=== JSON Schema 常见验证规则演示 ===\n");
        
        // 展示各种验证规则
        String comprehensiveSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "综合验证规则演示",
              "properties": {
                "字符串验证": {
                  "type": "string",
                  "minLength": 2,
                  "maxLength": 10,
                  "pattern": "^[a-zA-Z]+$"
                },
                "数字验证": {
                  "type": "number",
                  "minimum": 0,
                  "maximum": 100,
                  "multipleOf": 0.5
                },
                "整数验证": {
                  "type": "integer",
                  "minimum": 1,
                  "maximum": 10
                },
                "布尔验证": {
                  "type": "boolean"
                },
                "数组验证": {
                  "type": "array",
                  "items": {
                    "type": "string"
                  },
                  "minItems": 1,
                  "maxItems": 5,
                  "uniqueItems": true
                },
                "对象验证": {
                  "type": "object",
                  "properties": {
                    "nestedField": {
                      "type": "string"
                    }
                  },
                  "required": ["nestedField"]
                },
                "枚举验证": {
                  "type": "string",
                  "enum": ["option1", "option2", "option3"]
                },
                "格式验证": {
                  "type": "string",
                  "format": "email"
                }
              },
              "required": ["字符串验证", "数字验证", "整数验证", "布尔验证"]
            }
            """;
        
        System.out.println("综合验证规则 Schema:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(comprehensiveSchemaJson)));
        
        System.out.println("\n=== 各种验证规则说明 ===");
        System.out.println("📝 字符串验证：minLength, maxLength, pattern（正则表达式）");
        System.out.println("🔢 数字验证：minimum, maximum, multipleOf（倍数）");
        System.out.println("📊 数组验证：minItems, maxItems, uniqueItems（唯一性）");
        System.out.println("📦 对象验证：properties, required（嵌套对象和必填字段）");
        System.out.println("🎯 枚举验证：enum（限定可选值）");
        System.out.println("📧 格式验证：format（email, date, uri 等预定义格式）");
    }
    
    /**
     * 演示 4：在我们项目中的应用场景
     */
    @Test
    void demonstrateProjectUsage() throws Exception {
        System.out.println("\n=== 在我们项目中的应用场景 ===\n");
        
        // 1. simpleChat 方法的参数 Schema
        String simpleChatSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "simpleChat 方法参数",
              "description": "聊天接口的请求参数",
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
            """;
        
        System.out.println("1. simpleChat 方法参数 Schema（我们自动生成的）:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(simpleChatSchemaJson)));
        
        // 2. 展示如何在实际项目中使用
        System.out.println("\n=== 实际项目中的应用 ===");
        System.out.println("🎯 自动生成：根据 Java 方法签名自动生成 JSON Schema");
        System.out.println("🔍 参数验证：在 Controller 中验证请求参数格式");
        System.out.println("📚 API 文档：自动生成 Swagger/OpenAPI 文档");
        System.out.println("🧪 测试数据：根据 Schema 生成测试用例");
        System.out.println("🛡️ 类型安全：确保前后端数据格式一致");
        
        // 3. 展示验证流程
        System.out.println("\n=== 验证流程 ===");
        System.out.println("1️⃣ 客户端发送请求 → 2️⃣ 根据 Schema 验证参数 → 3️⃣ 处理业务逻辑 → 4️⃣ 返回结果");
    }
}
