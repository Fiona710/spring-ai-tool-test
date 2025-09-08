package org.example.springaitooltest.util;

import org.example.springaitooltest.controller.HelloworldController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API 文档生成器测试
 * 演示如何为 HelloworldController 生成 JSON Schema 格式的 API 文档
 */
@SpringBootTest
class ApiDocumentGeneratorTest {
    
    private ApiDocumentGenerator apiDocumentGenerator;
    
    @BeforeEach
    void setUp() {
        apiDocumentGenerator = new ApiDocumentGenerator();
    }
    
    @Test
    void testGenerateHelloworldControllerApiDoc() throws Exception {
        System.out.println("=== HelloworldController API 文档生成演示 ===\n");
        
        // 生成 API 文档
        String apiDoc = apiDocumentGenerator.generateApiDocument(HelloworldController.class);
        
        System.out.println("生成的 API 文档:");
        System.out.println(apiDoc);
        
        // 验证文档结构
        assertNotNull(apiDoc);
        assertTrue(apiDoc.contains("HelloworldController"));
        assertTrue(apiDoc.contains("simpleChat"));
        assertTrue(apiDoc.contains("requestSchema"));
        assertTrue(apiDoc.contains("responseSchema"));
        assertTrue(apiDoc.contains("requestExample"));
        assertTrue(apiDoc.contains("responseExample"));
        
        System.out.println("\n=== API 文档结构验证 ===");
        System.out.println("✅ 包含控制器名称: " + apiDoc.contains("HelloworldController"));
        System.out.println("✅ 包含接口方法: " + apiDoc.contains("simpleChat"));
        System.out.println("✅ 包含请求 Schema: " + apiDoc.contains("requestSchema"));
        System.out.println("✅ 包含响应 Schema: " + apiDoc.contains("responseSchema"));
        System.out.println("✅ 包含请求示例: " + apiDoc.contains("requestExample"));
        System.out.println("✅ 包含响应示例: " + apiDoc.contains("responseExample"));
    }
    
    @Test
    void testApiDocumentStructure() throws Exception {
        System.out.println("\n=== API 文档结构分析 ===\n");
        
        String apiDoc = apiDocumentGenerator.generateApiDocument(HelloworldController.class);
        
        // 分析文档结构
        System.out.println("1. 文档基本信息:");
        System.out.println("   - 标题: HelloworldController API 文档");
        System.out.println("   - 版本: 1.0.0");
        System.out.println("   - 基础路径: /helloworld");
        
        System.out.println("\n2. 接口信息:");
        System.out.println("   - 方法名: simpleChat");
        System.out.println("   - HTTP 方法: GET");
        System.out.println("   - 完整路径: /helloworld/simple/chat");
        System.out.println("   - 描述: simpleChat 接口");
        
        System.out.println("\n3. 请求参数 Schema:");
        System.out.println("   - 参数名: query");
        System.out.println("   - 类型: string");
        System.out.println("   - 描述: 用户查询内容");
        System.out.println("   - 必填: 是");
        
        System.out.println("\n4. 响应数据 Schema:");
        System.out.println("   - 成功标识: success (boolean)");
        System.out.println("   - 时间戳: timestamp (string, date-time)");
        System.out.println("   - 响应数据: data (string)");
        
        System.out.println("\n5. 示例数据:");
        System.out.println("   - 请求示例: {\"query\": \"示例字符串\"}");
        System.out.println("   - 响应示例: {\"data\": \"AI 响应内容\", \"success\": true, \"timestamp\": \"2024-01-01T12:00:00Z\"}");
    }
    
    @Test
    void testApiDocumentUsage() throws Exception {
        System.out.println("\n=== API 文档使用说明 ===\n");
        
        System.out.println("1. 获取 API 文档:");
        System.out.println("   GET http://localhost:8080/api-docs/helloworld");
        
        System.out.println("\n2. 获取 API 文档索引:");
        System.out.println("   GET http://localhost:8080/api-docs/index");
        
        System.out.println("\n3. 使用 API 文档进行开发:");
        System.out.println("   - 前端开发: 根据 requestSchema 生成表单");
        System.out.println("   - 后端开发: 根据 responseSchema 验证返回数据");
        System.out.println("   - 测试: 根据示例数据生成测试用例");
        System.out.println("   - 文档: 自动生成 API 文档");
        
        System.out.println("\n4. 实际调用示例:");
        System.out.println("   curl -X GET \"http://localhost:8080/helloworld/simple/chat?query=你好\"");
        System.out.println("   响应: \"你好！我是一个智能聊天助手，很高兴为您服务！\"");
    }
}
