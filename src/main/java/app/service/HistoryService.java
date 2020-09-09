package app.service;

import app.entity.LogsRep;
import app.exception.AppException;
import app.logging.ThreadLocalContext;
import app.repositories.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Service
@Slf4j
public class HistoryService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private HistoryRepository historyRepository;

    @Transactional
    public void save(final LogsRep request) {
        historyRepository.saveAndFlush(request);
    }

    public void fillRequestHistoryWithRequest(JoinPoint joinPoint) {
        LogsRep requestHistoryDto = new LogsRep();
        requestHistoryDto.setOperation(joinPoint.getSignature().getName());
        requestHistoryDto.setHost(joinPoint.getSignature().getDeclaringType().getSimpleName());
        // HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        requestHistoryDto.setToken(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("gradingServiceAccessToken"));
        requestHistoryDto.setRequestTime(LocalDateTime.now());
        if (joinPoint.getArgs() != null) {
            requestHistoryDto.setRequestBody(Arrays.toString(joinPoint.getArgs()));
        }
        ThreadLocalContext.getProcessingInfo().setLogsRep(requestHistoryDto);
        save(requestHistoryDto);
    }

    public void fillRequestHistoryWithResponse(Object retVal) {
        LogsRep requestHistoryDto = ThreadLocalContext.getProcessingInfo().getLogsRep();
        final ResponseEntity retVal1 = (ResponseEntity) retVal;
        requestHistoryDto.setResponseBody(String.valueOf(retVal1.getBody()));
        requestHistoryDto.setResponseCode(String.valueOf(retVal1.getStatusCodeValue()));
        requestHistoryDto.setResponseTime(LocalDateTime.now());
        save(requestHistoryDto);
    }

    public void fillRequestHistoryWithError(Exception ex) {
        LogsRep requestHistoryDto = ThreadLocalContext.getProcessingInfo().getLogsRep();
        requestHistoryDto.setResponseBody(ex.getMessage());
        requestHistoryDto.setResponseCode(ex instanceof AppException ? ((AppException) ex).getErrorCode() : "Unknown ERROR");
        requestHistoryDto.setResponseTime(LocalDateTime.now());
        save(requestHistoryDto);
    }

}
