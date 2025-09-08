package org.example.springaitooltest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * API 文档生成器
 * 根据 Spring Boot Controller 生成 JSON Schema 格式的 API 文档
 */
@Component
public class ApiDocumentGenerator {
    
    private final ObjectMapper objectMapper;
    private final JsonSchemaGenerator schemaGenerator;
    
    public ApiDocumentGenerator() {
        this.objectMapper = new ObjectMapper();
        this.schemaGenerator = new JsonSchemaGenerator();
    }
    
    /**
     * 为指定控制器生成完整的 API 文档
     */
    public String generateApiDocument(Class<?> controllerClass) {
        try {
            ObjectNode apiDoc = objectMapper.createObjectNode();
            
            // 基本信息
            apiDoc.put("$schema", "http://json-schema.org/draft-07/schema#");
            apiDoc.put("type", "object");
            apiDoc.put("title", controllerClass.getSimpleName() + " API 文档");
            apiDoc.put("description", "基于 JSON Schema 的 API 接口文档");
            apiDoc.put("version", "1.0.0");
            
            // 获取控制器基本信息
            String basePath = getBasePath(controllerClass);
            apiDoc.put("basePath", basePath);
            
            // 生成接口列表
            ArrayNode endpoints = objectMapper.createArrayNode();
            Method[] methods = controllerClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (isApiMethod(method)) {
                    ObjectNode endpoint = generateEndpointDocument(method, basePath);
                    endpoints.add(endpoint);
                }
            }
            
            apiDoc.set("endpoints", endpoints);
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiDoc);
            
        } catch (Exception e) {
            throw new RuntimeException("生成 API 文档失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成单个接口的文档
     */
    private ObjectNode generateEndpointDocument(Method method, String basePath) throws Exception {
        ObjectNode endpoint = objectMapper.createObjectNode();
        
        // 基本信息
        endpoint.put("methodName", method.getName());
        endpoint.put("httpMethod", getHttpMethod(method));
        endpoint.put("path", basePath + getMethodPath(method));
        endpoint.put("description", getMethodDescription(method));
        
        // 请求参数 Schema
        if (method.getParameterCount() > 0) {
            String requestSchema = schemaGenerator.generateSchema(method);
            endpoint.set("requestSchema", objectMapper.readTree(requestSchema));
            
            // 请求示例
            String requestExample = schemaGenerator.generateExampleJson(method);
            endpoint.set("requestExample", objectMapper.readTree(requestExample));
        }
        
        // 响应 Schema
        ObjectNode responseSchema = generateResponseSchema(method);
        endpoint.set("responseSchema", responseSchema);
        
        // 响应示例
        ObjectNode responseExample = generateResponseExample(method);
        endpoint.set("responseExample", responseExample);
        
        return endpoint;
    }
    
    /**
     * 生成响应 Schema
     */
    private ObjectNode generateResponseSchema(Method method) {
        ObjectNode responseSchema = objectMapper.createObjectNode();
        responseSchema.put("$schema", "http://json-schema.org/draft-07/schema#");
        responseSchema.put("type", "object");
        responseSchema.put("title", method.getName() + " 响应数据");
        
        ObjectNode properties = objectMapper.createObjectNode();
        
        // 根据返回类型生成响应结构
        Class<?> returnType = method.getReturnType();
        if (returnType == String.class) {
            ObjectNode dataProperty = objectMapper.createObjectNode();
            dataProperty.put("type", "string");
            dataProperty.put("description", "AI 聊天响应内容");
            dataProperty.put("example", "你好！我是一个智能聊天助手，很高兴为您服务！");
            properties.set("data", dataProperty);
        } else if (returnType == void.class) {
            ObjectNode messageProperty = objectMapper.createObjectNode();
            messageProperty.put("type", "string");
            messageProperty.put("description", "操作结果消息");
            messageProperty.put("example", "操作成功");
            properties.set("message", messageProperty);
        } else {
            // 复杂对象类型
            ObjectNode dataProperty = objectMapper.createObjectNode();
            dataProperty.put("type", "object");
            dataProperty.put("description", "响应数据对象");
            properties.set("data", dataProperty);
        }
        
        // 通用响应字段
        ObjectNode successProperty = objectMapper.createObjectNode();
        successProperty.put("type", "boolean");
        successProperty.put("description", "请求是否成功");
        successProperty.put("example", true);
        properties.set("success", successProperty);
        
        ObjectNode timestampProperty = objectMapper.createObjectNode();
        timestampProperty.put("type", "string");
        timestampProperty.put("format", "date-time");
        timestampProperty.put("description", "响应时间戳");
        timestampProperty.put("example", "2024-01-01T12:00:00Z");
        properties.set("timestamp", timestampProperty);
        
        responseSchema.set("properties", properties);
        
        ArrayNode required = objectMapper.createArrayNode();
        required.add("success");
        required.add("timestamp");
        if (returnType == String.class) {
            required.add("data");
        }
        responseSchema.set("required", required);
        
        return responseSchema;
    }
    
    /**
     * 生成响应示例
     */
    private ObjectNode generateResponseExample(Method method) {
        ObjectNode example = objectMapper.createObjectNode();
        
        Class<?> returnType = method.getReturnType();
        if (returnType == String.class) {
            example.put("data", "你好！我是一个智能聊天助手，很高兴为您服务！");
        } else if (returnType == void.class) {
            example.put("message", "操作成功");
        } else {
            ObjectNode data = objectMapper.createObjectNode();
            data.put("id", "123");
            data.put("name", "示例数据");
            example.set("data", data);
        }
        
        example.put("success", true);
        example.put("timestamp", "2024-01-01T12:00:00Z");
        
        return example;
    }
    
    /**
     * 获取控制器的基础路径
     */
    private String getBasePath(Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        }
        return "/" + controllerClass.getSimpleName().toLowerCase().replace("controller", "");
    }
    
    /**
     * 获取方法的 HTTP 方法
     */
    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PutMapping.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class)) {
            return "DELETE";
        }
        return "GET"; // 默认
    }
    
    /**
     * 获取方法的路径
     */
    private String getMethodPath(Method method) {
        if (method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class)) {
            org.springframework.web.bind.annotation.GetMapping mapping = 
                method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
            if (mapping.value().length > 0) {
                return mapping.value()[0];
            }
        } else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class)) {
            org.springframework.web.bind.annotation.PostMapping mapping = 
                method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class);
            if (mapping.value().length > 0) {
                return mapping.value()[0];
            }
        }
        return "/" + method.getName();
    }
    
    /**
     * 获取方法描述
     */
    private String getMethodDescription(Method method) {
        // 可以根据注解或其他方式获取描述
        return method.getName() + " 接口";
    }
    
    /**
     * 判断是否为 API 方法
     */
    private boolean isApiMethod(Method method) {
        return method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class) ||
               method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class) ||
               method.isAnnotationPresent(org.springframework.web.bind.annotation.PutMapping.class) ||
               method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class);
    }
}
