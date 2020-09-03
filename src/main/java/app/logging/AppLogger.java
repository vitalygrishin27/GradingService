package app.logging;

import app.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AppLogger {

    @Autowired
    private HistoryService historyService;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {
        //All application controllers
    }

    @Before("controllerBean()")
    public void logMethodAccessBeforeOperation(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("Enter to: " + className + "." + methodName);
        ThreadLocalContext.getProcessingInfo().startTiming(className + "." + methodName);
        historyService.fillRequestHistoryWithRequest(joinPoint);
    }

    @AfterReturning(pointcut = "controllerBean()", returning = "retVal")
    public void logMethodAccessAfterOperation(JoinPoint joinPoint, Object retVal) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        historyService.fillRequestHistoryWithResponse(retVal);
        log.debug("Exit from: " + className + "." + methodName);
        ThreadLocalContext.getProcessingInfo().stopTiming(className + "." + methodName);
        ThreadLocalContext.getProcessingInfo().printTiming();
        ThreadLocalContext.drop();
    }

    @AfterThrowing(pointcut = "controllerBean()", throwing = "ex")
    public void logMethodAccessAfter(Exception ex) {
        historyService.fillRequestHistoryWithError(ex);
        ThreadLocalContext.drop();
    }
}
