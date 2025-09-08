package org.example.springaitooltest.util;

import org.example.springaitooltest.controller.HelloworldController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON Schema 生成器测试类
 */
@SpringBootTest
class JsonSchemaGeneratorTest {
    
    private JsonSchemaGenerator schemaGenerator;
    
    @BeforeEach
    void setUp() {
        schemaGenerator = new JsonSchemaGenerator();
    }
    
    @Test
    void testGenerateSchemaForSimpleChat() throws Exception {
        // 获取 simpleChat 方法
        Method simpleChatMethod = HelloworldController.class.getDeclaredMethod("simpleChat", String.class);
        
        // 生成 Schema
        String schema = schemaGenerator.generateSchema(simpleChatMethod);
        
        // 验证 Schema 不为空
        assertNotNull(schema);
        assertFalse(schema.trim().isEmpty());
        
        // 验证 Schema 包含必要的字段
        assertTrue(schema.contains("$schema"));
        assertTrue(schema.contains("type"));
        assertTrue(schema.contains("properties"));
        assertTrue(schema.contains("query"));
        assertTrue(schema.contains("string"));
        
        System.out.println("SimpleChat 方法的 JSON Schema:");
        System.out.println(schema);
    }
    
    @Test
    void testGenerateSchemaByClassNameAndMethodName() {
        String className = "org.example.springaitooltest.controller.HelloworldController";
        String methodName = "simpleChat";
        
        String schema = schemaGenerator.generateSchema(className, methodName);
        
        assertNotNull(schema);
        assertFalse(schema.trim().isEmpty());
        assertTrue(schema.contains("query"));
        assertTrue(schema.contains("string"));
        
        System.out.println("通过类名和方法名生成的 Schema:");
        System.out.println(schema);
    }
    
    @Test
    void testGenerateExampleJson() throws Exception {
        Method simpleChatMethod = HelloworldController.class.getDeclaredMethod("simpleChat", String.class);
        
        String exampleJson = schemaGenerator.generateExampleJson(simpleChatMethod);
        
        assertNotNull(exampleJson);
        assertFalse(exampleJson.trim().isEmpty());
        assertTrue(exampleJson.contains("query"));
        assertTrue(exampleJson.contains("示例字符串"));
        
        System.out.println("SimpleChat 方法的示例 JSON:");
        System.out.println(exampleJson);
    }
    
    @Test
    void testGenerateSchemaForNonExistentMethod() {
        String className = "org.example.springaitooltest.controller.HelloworldController";
        String methodName = "nonExistentMethod";
        
        assertThrows(RuntimeException.class, () -> {
            schemaGenerator.generateSchema(className, methodName);
        });
    }
    
    @Test
    void testGenerateSchemaForNonExistentClass() {
        String className = "org.example.NonExistentClass";
        String methodName = "someMethod";
        
        assertThrows(RuntimeException.class, () -> {
            schemaGenerator.generateSchema(className, methodName);
        });
    }
    
    @Test
    void testSchemaStructure() throws Exception {
        Method simpleChatMethod = HelloworldController.class.getDeclaredMethod("simpleChat", String.class);
        String schema = schemaGenerator.generateSchema(simpleChatMethod);
        
        // 验证 JSON 结构
        assertTrue(schema.contains("\"$schema\""));
        assertTrue(schema.contains("\"type\" : \"object\""));
        assertTrue(schema.contains("\"properties\""));
        assertTrue(schema.contains("\"query\""));
        
        // 验证参数类型
        assertTrue(schema.contains("\"type\" : \"string\""));
    }
}
