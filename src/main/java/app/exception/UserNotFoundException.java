package app.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppTechnicalException {
    public UserNotFoundException(Long userId) {
        super("ERR-001", HttpStatus.BAD_REQUEST, userId);
    }
}
