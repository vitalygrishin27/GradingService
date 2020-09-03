package app;

import app.exception.AppException;
import app.logging.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<Object> handleSlaApplicationException(AppException e) {
        log.debug("AppExceptionHandler: " + e.getDetails(), e);
        ThreadLocalContext.getProcessingInfo().stopAllTimers();
        ThreadLocalContext.getProcessingInfo().printTiming();
        ThreadLocalContext.drop();
        return new ResponseEntity<>(e.getErrorMessage(), e.getStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        log.debug("AppExceptionHandler: " + e.getMessage(), e);
        ThreadLocalContext.getProcessingInfo().stopAllTimers();
        ThreadLocalContext.getProcessingInfo().printTiming();
        ThreadLocalContext.drop();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

