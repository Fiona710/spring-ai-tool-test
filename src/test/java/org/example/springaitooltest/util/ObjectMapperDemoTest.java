package org.example.springaitooltest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ObjectMapper 使用演示测试
 * 详细解析 objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString() 的使用
 */
@SpringBootTest
class ObjectMapperDemoTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    void demonstrateObjectMapperUsage() throws Exception {
        System.out.println("=== ObjectMapper 使用演示 ===\n");
        
        // 1. 原始 JSON 字符串（压缩格式）
        String simpleChatSchemaJson = """
            {"$schema":"http://json-schema.org/draft-07/schema#","type":"object","title":"simpleChat 方法参数","description":"聊天接口的请求参数","properties":{"query":{"type":"string","description":"用户查询内容","minLength":1,"maxLength":1000}},"required":["query"]}
            """;
        
        System.out.println("1. 原始 JSON 字符串（压缩格式）:");
        System.out.println(simpleChatSchemaJson);
        System.out.println("长度: " + simpleChatSchemaJson.length() + " 字符");
        
        // 2. 解析为 JsonNode 对象
        JsonNode jsonNode = objectMapper.readTree(simpleChatSchemaJson);
        System.out.println("\n2. 解析为 JsonNode 对象:");
        System.out.println("JsonNode 类型: " + jsonNode.getClass().getSimpleName());
        System.out.println("是否为对象: " + jsonNode.isObject());
        System.out.println("包含的字段: " + jsonNode.fieldNames().next());
        
        // 3. 转换为格式化的 JSON 字符串
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(jsonNode);
        
        System.out.println("\n3. 转换为格式化的 JSON 字符串:");
        System.out.println(formattedJson);
        System.out.println("长度: " + formattedJson.length() + " 字符");
        
        // 4. 对比不同输出方式
        System.out.println("\n=== 不同输出方式对比 ===");
        
        // 方式 1：直接 toString()（不推荐）
        String toStringResult = jsonNode.toString();
        System.out.println("方式 1 - toString():");
        System.out.println(toStringResult);
        
        // 方式 2：writeValueAsString()（压缩格式）
        String compactResult = objectMapper.writeValueAsString(jsonNode);
        System.out.println("\n方式 2 - writeValueAsString()（压缩格式）:");
        System.out.println(compactResult);
        
        // 方式 3：writerWithDefaultPrettyPrinter().writeValueAsString()（美化格式）
        String prettyResult = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(jsonNode);
        System.out.println("\n方式 3 - writerWithDefaultPrettyPrinter().writeValueAsString()（美化格式）:");
        System.out.println(prettyResult);
    }
    
    @Test
    void demonstrateStepByStepProcess() throws Exception {
        System.out.println("\n=== 逐步解析过程 ===\n");
        
        String jsonString = """
            {"name":"张三","age":25,"email":"zhangsan@example.com","hobbies":["读书","游泳","编程"]}
            """;
        
        System.out.println("步骤 1: 原始 JSON 字符串");
        System.out.println(jsonString);
        
        System.out.println("\n步骤 2: 解析为 JsonNode");
        JsonNode node = objectMapper.readTree(jsonString);
        System.out.println("JsonNode 对象: " + node);
        System.out.println("节点类型: " + node.getNodeType());
        System.out.println("字段数量: " + node.size());
        
        System.out.println("\n步骤 3: 访问 JsonNode 中的数据");
        System.out.println("name: " + node.get("name").asText());
        System.out.println("age: " + node.get("age").asInt());
        System.out.println("email: " + node.get("email").asText());
        System.out.println("hobbies: " + node.get("hobbies"));
        
        System.out.println("\n步骤 4: 创建美化写入器");
        var writer = objectMapper.writerWithDefaultPrettyPrinter();
        System.out.println("写入器类型: " + writer.getClass().getSimpleName());
        
        System.out.println("\n步骤 5: 转换为美化格式的 JSON 字符串");
        String prettyJson = writer.writeValueAsString(node);
        System.out.println(prettyJson);
    }
    
    @Test
    void demonstrateDifferentWriters() throws Exception {
        System.out.println("\n=== 不同写入器对比 ===\n");
        
        String jsonString = """
            {"server":{"port":8080,"contextPath":"/api"},"spring":{"application":{"name":"test-app"},"ai":{"dashscope":{"api-key":"sk-123"}}}}
            """;
        
        JsonNode node = objectMapper.readTree(jsonString);
        
        System.out.println("1. 默认写入器（压缩格式）:");
        String defaultResult = objectMapper.writeValueAsString(node);
        System.out.println(defaultResult);
        
        System.out.println("\n2. 美化写入器（默认缩进）:");
        String prettyResult = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(node);
        System.out.println(prettyResult);
        
        System.out.println("\n3. 自定义缩进写入器:");
        String customResult = objectMapper.writerWithDefaultPrettyPrinter()
            .withRootValueSeparator("\n")
            .writeValueAsString(node);
        System.out.println(customResult);
    }
    
    @Test
    void demonstrateErrorHandling() throws Exception {
        System.out.println("\n=== 错误处理演示 ===\n");
        
        // 1. 有效的 JSON
        String validJson = """
            {"name":"张三","age":25}
            """;
        
        try {
            JsonNode validNode = objectMapper.readTree(validJson);
            String result = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(validNode);
            System.out.println("有效 JSON 处理结果:");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("处理有效 JSON 时出错: " + e.getMessage());
        }
        
        // 2. 无效的 JSON
        String invalidJson = """
            {"name":"张三","age":25,}
            """;
        
        try {
            JsonNode invalidNode = objectMapper.readTree(invalidJson);
            String result = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(invalidNode);
            System.out.println("无效 JSON 处理结果:");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("处理无效 JSON 时出错: " + e.getMessage());
        }
    }
}
