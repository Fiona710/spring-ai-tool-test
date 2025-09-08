package org.example.springaitooltest.controller;

import org.example.springaitooltest.util.ApiDocumentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * API 文档生成控制器
 * 提供基于 JSON Schema 的 API 文档生成服务
 */
@RestController
@RequestMapping("/api-docs")
public class ApiDocController {
    
    @Autowired
    private ApiDocumentGenerator apiDocumentGenerator;
    
    /**
     * 生成 HelloworldController 的 API 文档
     */
    @GetMapping("/helloworld")
    public String generateHelloworldApiDoc() {
        try {
            return apiDocumentGenerator.generateApiDocument(
                org.example.springaitooltest.controller.HelloworldController.class
            );
        } catch (Exception e) {
            return "生成 API 文档失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成指定控制器的 API 文档
     */
    @GetMapping("/controller/{className}")
    public String generateControllerApiDoc(@PathVariable String className) {
        try {
            Class<?> controllerClass = Class.forName(
                "org.example.springaitooltest.controller." + className
            );
            return apiDocumentGenerator.generateApiDocument(controllerClass);
        } catch (ClassNotFoundException e) {
            return "未找到控制器类: " + className;
        } catch (Exception e) {
            return "生成 API 文档失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成所有控制器的 API 文档索引
     */
    @GetMapping("/index")
    public String generateApiDocIndex() {
        try {
            StringBuilder index = new StringBuilder();
            index.append("{\n");
            index.append("  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n");
            index.append("  \"type\": \"object\",\n");
            index.append("  \"title\": \"API 文档索引\",\n");
            index.append("  \"description\": \"Spring AI Tool Test 项目 API 文档索引\",\n");
            index.append("  \"version\": \"1.0.0\",\n");
            index.append("  \"baseUrl\": \"http://localhost:8080\",\n");
            index.append("  \"controllers\": [\n");
            index.append("    {\n");
            index.append("      \"name\": \"HelloworldController\",\n");
            index.append("      \"description\": \"智能聊天控制器\",\n");
            index.append("      \"basePath\": \"/helloworld\",\n");
            index.append("      \"docUrl\": \"/api-docs/helloworld\",\n");
            index.append("      \"endpoints\": [\n");
            index.append("        {\n");
            index.append("          \"name\": \"simpleChat\",\n");
            index.append("          \"method\": \"GET\",\n");
            index.append("          \"path\": \"/helloworld/simple/chat\",\n");
            index.append("          \"description\": \"简单聊天接口\"\n");
            index.append("        }\n");
            index.append("      ]\n");
            index.append("    },\n");
            index.append("    {\n");
            index.append("      \"name\": \"SchemaController\",\n");
            index.append("      \"description\": \"JSON Schema 生成控制器\",\n");
            index.append("      \"basePath\": \"/schema\",\n");
            index.append("      \"docUrl\": \"/api-docs/schema\",\n");
            index.append("      \"endpoints\": [\n");
            index.append("        {\n");
            index.append("          \"name\": \"generateSimpleChatSchema\",\n");
            index.append("          \"method\": \"GET\",\n");
            index.append("          \"path\": \"/schema/simplechat\",\n");
            index.append("          \"description\": \"生成 simpleChat 方法的 JSON Schema\"\n");
            index.append("        },\n");
            index.append("        {\n");
            index.append("          \"name\": \"generateSimpleChatExample\",\n");
            index.append("          \"method\": \"GET\",\n");
            index.append("          \"path\": \"/schema/simplechat/example\",\n");
            index.append("          \"description\": \"生成 simpleChat 方法的示例 JSON\"\n");
            index.append("        }\n");
            index.append("      ]\n");
            index.append("    },\n");
            index.append("    {\n");
            index.append("      \"name\": \"ApiDocController\",\n");
            index.append("      \"description\": \"API 文档生成控制器\",\n");
            index.append("      \"basePath\": \"/api-docs\",\n");
            index.append("      \"docUrl\": \"/api-docs/api-docs\",\n");
            index.append("      \"endpoints\": [\n");
            index.append("        {\n");
            index.append("          \"name\": \"generateHelloworldApiDoc\",\n");
            index.append("          \"method\": \"GET\",\n");
            index.append("          \"path\": \"/api-docs/helloworld\",\n");
            index.append("          \"description\": \"生成 HelloworldController 的 API 文档\"\n");
            index.append("        },\n");
            index.append("        {\n");
            index.append("          \"name\": \"generateApiDocIndex\",\n");
            index.append("          \"method\": \"GET\",\n");
            index.append("          \"path\": \"/api-docs/index\",\n");
            index.append("          \"description\": \"生成 API 文档索引\"\n");
            index.append("        }\n");
            index.append("      ]\n");
            index.append("    }\n");
            index.append("  ]\n");
            index.append("}\n");
            
            return index.toString();
        } catch (Exception e) {
            return "生成 API 文档索引失败: " + e.getMessage();
        }
    }
}
