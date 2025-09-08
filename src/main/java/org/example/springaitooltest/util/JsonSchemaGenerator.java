package org.example.springaitooltest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON Schema 生成器工具类
 * 根据 Java 方法签名自动生成对应的 JSON Schema
 */
@Component
public class JsonSchemaGenerator {
    
    private final ObjectMapper objectMapper;
    
    public JsonSchemaGenerator() {
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 根据方法签名生成 JSON Schema
     * 
     * @param method 目标方法
     * @return JSON Schema 字符串
     */
    public String generateSchema(Method method) {
        try {
            ObjectNode schema = objectMapper.createObjectNode();
            
            // 设置基本 schema 信息
            schema.put("$schema", "http://json-schema.org/draft-07/schema#");
            schema.put("type", "object");
            schema.put("title", method.getName() + " 方法参数 Schema");
            schema.put("description", "自动生成的 " + method.getName() + " 方法参数 JSON Schema");
            
            // 生成属性定义
            ObjectNode properties = objectMapper.createObjectNode();
            com.fasterxml.jackson.databind.node.ArrayNode required = objectMapper.createArrayNode();
            
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                ObjectNode paramSchema = generateParameterSchema(parameter);
                properties.set(paramName, paramSchema);
                
                // 检查参数是否必需（这里简化处理，实际可能需要更复杂的逻辑）
                if (!isOptionalParameter(parameter)) {
                    required.add(paramName);
                }
            }
            
            schema.set("properties", properties);
            if (required.size() > 0) {
                schema.set("required", required);
            }
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
            
        } catch (Exception e) {
            throw new RuntimeException("生成 JSON Schema 失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成单个参数的 Schema
     */
    private ObjectNode generateParameterSchema(Parameter parameter) {
        ObjectNode paramSchema = objectMapper.createObjectNode();
        
        Class<?> paramType = parameter.getType();
        String typeName = getJsonSchemaType(paramType);
        
        paramSchema.put("type", typeName);
        paramSchema.put("description", "参数: " + parameter.getName() + " (" + paramType.getSimpleName() + ")");
        
        // 根据类型添加特定属性
        addTypeSpecificProperties(paramSchema, paramType);
        
        return paramSchema;
    }
    
    /**
     * 将 Java 类型映射为 JSON Schema 类型
     */
    private String getJsonSchemaType(Class<?> type) {
        if (type == String.class) {
            return "string";
        } else if (type == Integer.class || type == int.class) {
            return "integer";
        } else if (type == Long.class || type == long.class) {
            return "integer";
        } else if (type == Double.class || type == double.class) {
            return "number";
        } else if (type == Float.class || type == float.class) {
            return "number";
        } else if (type == Boolean.class || type == boolean.class) {
            return "boolean";
        } else if (type.isArray()) {
            return "array";
        } else if (Map.class.isAssignableFrom(type)) {
            return "object";
        } else {
            // 对于复杂对象，默认为 object 类型
            return "object";
        }
    }
    
    /**
     * 根据类型添加特定属性
     */
    private void addTypeSpecificProperties(ObjectNode schema, Class<?> type) {
        if (type == String.class) {
            // 字符串类型可以添加长度限制等
            schema.put("minLength", 0);
        } else if (type == Integer.class || type == int.class) {
            // 整数类型可以添加范围限制
            schema.put("minimum", Integer.MIN_VALUE);
            schema.put("maximum", Integer.MAX_VALUE);
        } else if (type == Long.class || type == long.class) {
            schema.put("minimum", Long.MIN_VALUE);
            schema.put("maximum", Long.MAX_VALUE);
        } else if (type == Double.class || type == double.class || 
                   type == Float.class || type == float.class) {
            // 数字类型
            schema.put("minimum", Double.MIN_VALUE);
            schema.put("maximum", Double.MAX_VALUE);
        }
    }
    
    /**
     * 判断参数是否为可选参数
     * 这里简化处理，实际项目中可能需要更复杂的逻辑
     */
    private boolean isOptionalParameter(Parameter parameter) {
        // 检查是否有 @Nullable 注解或其他表示可选的注解
        return parameter.getAnnotations().length > 0 && 
               parameter.getAnnotations()[0].annotationType().getSimpleName().contains("Nullable");
    }
    
    /**
     * 根据类名和方法名生成 Schema
     */
    public String generateSchema(String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            Method method = findMethod(clazz, methodName);
            if (method == null) {
                throw new IllegalArgumentException("未找到方法: " + methodName + " 在类 " + className);
            }
            return generateSchema(method);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到类: " + className, e);
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
    
    /**
     * 生成示例 JSON 数据
     */
    public String generateExampleJson(Method method) {
        try {
            ObjectNode example = objectMapper.createObjectNode();
            
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                Object exampleValue = generateExampleValue(parameter.getType());
                example.set(paramName, objectMapper.valueToTree(exampleValue));
            }
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(example);
            
        } catch (Exception e) {
            throw new RuntimeException("生成示例 JSON 失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成示例值
     */
    private Object generateExampleValue(Class<?> type) {
        if (type == String.class) {
            return "示例字符串";
        } else if (type == Integer.class || type == int.class) {
            return 123;
        } else if (type == Long.class || type == long.class) {
            return 123L;
        } else if (type == Double.class || type == double.class) {
            return 123.45;
        } else if (type == Float.class || type == float.class) {
            return 123.45f;
        } else if (type == Boolean.class || type == boolean.class) {
            return true;
        } else if (type.isArray()) {
            return new Object[0];
        } else {
            return new HashMap<>();
        }
    }
}
