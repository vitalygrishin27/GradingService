package app;

import app.entity.Role;
import app.entity.User;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GradingServiceMain {
    @Autowired
    UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(GradingServiceMain.class, args);
        User user = new User(0, "testLogin", "testEncryptedPassword", "password", Role.JURY, "testFirstName", "testSecondName", "testLastName", "testPosition", "testPhoto");
    }
}
