package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentManagementApplicationTests {
    
@Autowired
    private ApplicationContext context;
    
    @Test
    void contextLoads() {
        assertNotNull(context, "Le contexte de l'application ne devrait pas être nul");
    }

}
