package tooltest;

import org.example.springaitooltest.SpringAiToolTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = SpringAiToolTestApplication.class)
@TestPropertySource(properties = {
    "spring.ai.openai.api-key=test-key"
})
class DateTimeToolsTest {

    @Test
    void contextLoads() {
        // 这个测试验证Spring上下文能够正确加载包含@Tool注解的组件
    }
}
