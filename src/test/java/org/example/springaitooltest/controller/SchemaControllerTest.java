package org.example.springaitooltest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Schema 控制器测试类
 */
@WebMvcTest(SchemaController.class)
class SchemaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGenerateSimpleChatSchema() throws Exception {
        mockMvc.perform(get("/schema/simplechat"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("query")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("string")));
    }
    
    @Test
    void testGenerateSimpleChatExample() throws Exception {
        mockMvc.perform(get("/schema/simplechat/example"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("query")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("示例字符串")));
    }
    
    @Test
    void testGenerateSchemaWithParameters() throws Exception {
        String className = "org.example.springaitooltest.controller.HelloworldController";
        String methodName = "simpleChat";
        
        mockMvc.perform(get("/schema/generate")
                .param("className", className)
                .param("methodName", methodName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("query")));
    }
    
    @Test
    void testGetCompleteMethodInfo() throws Exception {
        String className = "org.example.springaitooltest.controller.HelloworldController";
        String methodName = "simpleChat";
        
        mockMvc.perform(get("/schema/complete")
                .param("className", className)
                .param("methodName", methodName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("methodName")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("schema")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("example")));
    }
}
