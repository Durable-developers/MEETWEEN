package meetween.backend.support.annotation;

import meetween.backend.authentication.application.AuthService;
import meetween.backend.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
public abstract class ServiceTest {
}
