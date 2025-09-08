package org.example.springaitooltest.controller;

import org.example.springaitooltest.util.JsonSchemaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * JSON Schema 生成控制器
 * 提供 REST API 来生成方法的 JSON Schema
 */
@RestController
@RequestMapping("/schema")
public class SchemaController {
    
    @Autowired
    private JsonSchemaGenerator schemaGenerator;
    
    /**
     * 根据类名和方法名生成 JSON Schema
     * 
     * @param className 类名（完整包名）
     * @param methodName 方法名
     * @return JSON Schema 字符串
     */
    @GetMapping("/generate")
    public String generateSchema(
            @RequestParam String className,
            @RequestParam String methodName) {
        try {
            return schemaGenerator.generateSchema(className, methodName);
        } catch (Exception e) {
            return "生成 Schema 失败: " + e.getMessage();
        }
    }
    
    /**
     * 为 simpleChat 方法生成 JSON Schema
     * 
     * @return simpleChat 方法的 JSON Schema
     */
    @GetMapping("/simplechat")
    public String generateSimpleChatSchema() {
        try {
            return schemaGenerator.generateSchema(
                "org.example.springaitooltest.controller.HelloworldController", 
                "simpleChat"
            );
        } catch (Exception e) {
            return "生成 simpleChat Schema 失败: " + e.getMessage();
        }
    }
    
    /**
     * 为 simpleChat 方法生成示例 JSON
     * 
     * @return simpleChat 方法的示例 JSON
     */
    @GetMapping("/simplechat/example")
    public String generateSimpleChatExample() {
        try {
            Class<?> clazz = Class.forName("org.example.springaitooltest.controller.HelloworldController");
            Method method = clazz.getDeclaredMethod("simpleChat", String.class);
            return schemaGenerator.generateExampleJson(method);
        } catch (Exception e) {
            return "生成 simpleChat 示例失败: " + e.getMessage();
        }
    }
    
    /**
     * 获取指定方法的完整信息（Schema + 示例）
     * 
     * @param className 类名
     * @param methodName 方法名
     * @return 包含 Schema 和示例的 JSON 对象
     */
    @GetMapping("/complete")
    public String getCompleteMethodInfo(
            @RequestParam String className,
            @RequestParam String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            Method method = findMethod(clazz, methodName);
            if (method == null) {
                return "未找到方法: " + methodName + " 在类 " + className;
            }
            
            String schema = schemaGenerator.generateSchema(method);
            String example = schemaGenerator.generateExampleJson(method);
            
            return String.format("{\n" +
                "  \"methodName\": \"%s\",\n" +
                "  \"className\": \"%s\",\n" +
                "  \"schema\": %s,\n" +
                "  \"example\": %s\n" +
                "}", methodName, className, schema, example);
                
        } catch (Exception e) {
            return "获取方法信息失败: " + e.getMessage();
        }
    }
    
    /**
     * 查找指定名称的方法
     */
    private Method findMethod(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
