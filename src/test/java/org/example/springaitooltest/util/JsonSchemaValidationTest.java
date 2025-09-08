package org.example.springaitooltest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON Schema 验证功能演示测试
 * 展示如何使用 JSON Schema 来验证 JSON 数据的结构和格式
 */
@SpringBootTest
class JsonSchemaValidationTest {
    
    private ObjectMapper objectMapper;
    private JsonSchemaFactory schemaFactory;
    
    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    }
    
    /**
     * 测试 1：用户注册表单的 JSON Schema 验证
     * 演示如何验证用户注册数据的格式
     */
    @Test
    void testUserRegistrationSchemaValidation() throws Exception {
        // 1. 定义用户注册的 JSON Schema
        String userSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "用户注册表单",
              "description": "用户注册时提交的数据格式",
              "properties": {
                "username": {
                  "type": "string",
                  "description": "用户名",
                  "minLength": 3,
                  "maxLength": 20,
                  "pattern": "^[a-zA-Z0-9_]+$"
                },
                "email": {
                  "type": "string",
                  "description": "邮箱地址",
                  "format": "email"
                },
                "password": {
                  "type": "string",
                  "description": "密码",
                  "minLength": 8,
                  "maxLength": 50
                },
                "age": {
                  "type": "integer",
                  "description": "年龄",
                  "minimum": 18,
                  "maximum": 100
                },
                "isVip": {
                  "type": "boolean",
                  "description": "是否为VIP用户"
                }
              },
              "required": ["username", "email", "password", "age"],
              "additionalProperties": false
            }
            """;
        
        // 2. 创建 JSON Schema 验证器
        JsonSchema schema = schemaFactory.getSchema(userSchemaJson);
        
        // 3. 测试用例 1：有效的数据
        String validUserJson = """
            {
              "username": "zhangsan123",
              "email": "zhangsan@example.com",
              "password": "password123",
              "age": 25,
              "isVip": true
            }
            """;
        
        JsonNode validUser = objectMapper.readTree(validUserJson);
        Set<ValidationMessage> errors = schema.validate(validUser);
        
        System.out.println("=== 测试用例 1：有效用户数据 ===");
        System.out.println("输入数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validUser));
        System.out.println("验证结果: " + (errors.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors.isEmpty()) {
            errors.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertTrue(errors.isEmpty(), "有效数据应该通过验证");
        
        // 4. 测试用例 2：缺少必填字段
        String invalidUserJson1 = """
            {
              "username": "lisi",
              "email": "lisi@example.com"
            }
            """;
        
        JsonNode invalidUser1 = objectMapper.readTree(invalidUserJson1);
        Set<ValidationMessage> errors1 = schema.validate(invalidUser1);
        
        System.out.println("\n=== 测试用例 2：缺少必填字段 ===");
        System.out.println("输入数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(invalidUser1));
        System.out.println("验证结果: " + (errors1.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors1.isEmpty()) {
            errors1.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertFalse(errors1.isEmpty(), "缺少必填字段应该验证失败");
        
        // 5. 测试用例 3：数据类型错误
        String invalidUserJson2 = """
            {
              "username": "wangwu",
              "email": "wangwu@example.com",
              "password": "password123",
              "age": "25岁"
            }
            """;
        
        JsonNode invalidUser2 = objectMapper.readTree(invalidUserJson2);
        Set<ValidationMessage> errors2 = schema.validate(invalidUser2);
        
        System.out.println("\n=== 测试用例 3：数据类型错误 ===");
        System.out.println("输入数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(invalidUser2));
        System.out.println("验证结果: " + (errors2.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors2.isEmpty()) {
            errors2.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertFalse(errors2.isEmpty(), "数据类型错误应该验证失败");
    }
    
    /**
     * 测试 2：API 请求参数的 JSON Schema 验证
     * 演示如何验证 API 请求参数格式
     */
    @Test
    void testApiRequestSchemaValidation() throws Exception {
        // 1. 定义 API 请求的 JSON Schema（类似我们的 simpleChat 方法）
        String apiRequestSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "聊天API请求参数",
              "description": "聊天接口的请求参数格式",
              "properties": {
                "query": {
                  "type": "string",
                  "description": "用户查询内容",
                  "minLength": 1,
                  "maxLength": 1000
                },
                "userId": {
                  "type": "string",
                  "description": "用户ID",
                  "pattern": "^[a-zA-Z0-9_-]+$"
                },
                "sessionId": {
                  "type": "string",
                  "description": "会话ID"
                },
                "options": {
                  "type": "object",
                  "description": "可选参数",
                  "properties": {
                    "temperature": {
                      "type": "number",
                      "minimum": 0.0,
                      "maximum": 2.0,
                      "default": 0.7
                    },
                    "maxTokens": {
                      "type": "integer",
                      "minimum": 1,
                      "maximum": 4000,
                      "default": 1000
                    }
                  }
                }
              },
              "required": ["query"],
              "additionalProperties": false
            }
            """;
        
        // 2. 创建 JSON Schema 验证器
        JsonSchema schema = schemaFactory.getSchema(apiRequestSchemaJson);
        
        // 3. 测试有效请求
        String validRequestJson = """
            {
              "query": "你好，请介绍一下你自己",
              "userId": "user_123",
              "sessionId": "session_456",
              "options": {
                "temperature": 0.8,
                "maxTokens": 500
              }
            }
            """;
        
        JsonNode validRequest = objectMapper.readTree(validRequestJson);
        Set<ValidationMessage> errors = schema.validate(validRequest);
        
        System.out.println("\n=== API 请求验证：有效请求 ===");
        System.out.println("请求数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validRequest));
        System.out.println("验证结果: " + (errors.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors.isEmpty()) {
            errors.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertTrue(errors.isEmpty(), "有效请求应该通过验证");
        
        // 4. 测试无效请求（查询内容为空）
        String invalidRequestJson = """
            {
              "query": "",
              "userId": "user_123"
            }
            """;
        
        JsonNode invalidRequest = objectMapper.readTree(invalidRequestJson);
        Set<ValidationMessage> errors2 = schema.validate(invalidRequest);
        
        System.out.println("\n=== API 请求验证：无效请求 ===");
        System.out.println("请求数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(invalidRequest));
        System.out.println("验证结果: " + (errors2.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors2.isEmpty()) {
            errors2.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertFalse(errors2.isEmpty(), "无效请求应该验证失败");
    }
    
    /**
     * 测试 3：配置文件格式验证
     * 演示如何验证应用配置文件的格式
     */
    @Test
    void testConfigFileSchemaValidation() throws Exception {
        // 1. 定义配置文件的 JSON Schema
        String configSchemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "title": "应用配置文件",
              "description": "Spring Boot 应用配置文件格式",
              "properties": {
                "server": {
                  "type": "object",
                  "properties": {
                    "port": {
                      "type": "integer",
                      "minimum": 1,
                      "maximum": 65535,
                      "default": 8080
                    },
                    "contextPath": {
                      "type": "string",
                      "pattern": "^/.*",
                      "default": "/"
                    }
                  },
                  "required": ["port"]
                },
                "spring": {
                  "type": "object",
                  "properties": {
                    "application": {
                      "type": "object",
                      "properties": {
                        "name": {
                          "type": "string",
                          "minLength": 1
                        }
                      },
                      "required": ["name"]
                    },
                    "ai": {
                      "type": "object",
                      "properties": {
                        "dashscope": {
                          "type": "object",
                          "properties": {
                            "api-key": {
                              "type": "string",
                              "minLength": 1
                            }
                          },
                          "required": ["api-key"]
                        }
                      }
                    }
                  },
                  "required": ["application", "ai"]
                }
              },
              "required": ["server", "spring"],
              "additionalProperties": true
            }
            """;
        
        // 2. 创建 JSON Schema 验证器
        JsonSchema schema = schemaFactory.getSchema(configSchemaJson);
        
        // 3. 测试有效配置
        String validConfigJson = """
            {
              "server": {
                "port": 8080,
                "contextPath": "/api"
              },
              "spring": {
                "application": {
                  "name": "spring-ai-tool-test"
                },
                "ai": {
                  "dashscope": {
                    "api-key": "sk-1234567890abcdef"
                  }
                }
              }
            }
            """;
        
        JsonNode validConfig = objectMapper.readTree(validConfigJson);
        Set<ValidationMessage> errors = schema.validate(validConfig);
        
        System.out.println("\n=== 配置文件验证：有效配置 ===");
        System.out.println("配置数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validConfig));
        System.out.println("验证结果: " + (errors.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors.isEmpty()) {
            errors.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertTrue(errors.isEmpty(), "有效配置应该通过验证");
    }
    
    /**
     * 测试 4：演示 JSON Schema 的常见验证规则
     */
    @Test
    void testCommonValidationRules() throws Exception {
        String schemaJson = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "type": "object",
              "properties": {
                "name": {
                  "type": "string",
                  "minLength": 2,
                  "maxLength": 50
                },
                "email": {
                  "type": "string",
                  "format": "email"
                },
                "age": {
                  "type": "integer",
                  "minimum": 0,
                  "maximum": 150
                },
                "phone": {
                  "type": "string",
                  "pattern": "^1[3-9]\\d{9}$"
                },
                "tags": {
                  "type": "array",
                  "items": {
                    "type": "string"
                  },
                  "minItems": 1,
                  "maxItems": 10,
                  "uniqueItems": true
                }
              },
              "required": ["name", "email"]
            }
            """;
        
        JsonSchema schema = schemaFactory.getSchema(schemaJson);
        
        // 测试各种验证规则
        String testDataJson = """
            {
              "name": "张三",
              "email": "zhangsan@example.com",
              "age": 25,
              "phone": "13812345678",
              "tags": ["java", "spring", "json"]
            }
            """;
        
        JsonNode testData = objectMapper.readTree(testDataJson);
        Set<ValidationMessage> errors = schema.validate(testData);
        
        System.out.println("\n=== 常见验证规则演示 ===");
        System.out.println("测试数据: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testData));
        System.out.println("验证结果: " + (errors.isEmpty() ? "✅ 验证通过" : "❌ 验证失败"));
        if (!errors.isEmpty()) {
            errors.forEach(error -> System.out.println("错误: " + error.getMessage()));
        }
        assertTrue(errors.isEmpty(), "测试数据应该通过验证");
    }
}
