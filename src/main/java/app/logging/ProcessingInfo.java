package app.logging;

import app.entity.LogsRep;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Slf4j
public class ProcessingInfo {

    private StackedKeyMap<String, Long> timings;
    @Setter
    @Getter
    private LogsRep logsRep;
    @Getter
    private String requestId;
    private boolean showCaption;
    private boolean replaceLineBreak;
    private int classNameSize;
    private boolean cutNeeded;
    private String operation;
    private String username;

    public ProcessingInfo() {
        timings = new StackedKeyMap<>();
    }

    public String format(ILoggingEvent event) {
        StringBuffer buffer = new StringBuffer();
        printField(buffer, "Date", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date(event.getTimeStamp())));
        printField(buffer, "Level", event.getLevel());
        printField(buffer, "Class", cutFromEnd(event.getLoggerName()));
        printField(buffer, "Operation", operation);
        printField(buffer, "RequestId", requestId);
        printField(buffer, "UserName", username);
        printField(buffer, "Message", replaceMessage(event.getFormattedMessage()));
        String stackTrace = getStackTrace(event);
        if (stackTrace != null) {
            printField(buffer, "Stacktrace", stackTrace);
        }
        return buffer.toString();
    }

    private void printField(StringBuffer buf, String caption, Object value) {
        if (showCaption) {
            if (value != null && StringUtils.isNotEmpty(value.toString())) {
                buf.append("[").append(caption).append(": ").append(value).append("]");
            }
        } else {
            buf.append("[").append(value == null ? StringUtils.EMPTY : value).append("]");
        }
    }

    private String replaceMessage(String message) {
        if (replaceLineBreak) {
            return message.replaceAll(StringUtils.CR, StringUtils.EMPTY).replaceAll(StringUtils.LF, "<br/>");
        } else {
            return message;
        }
    }

    private String getStackTrace(ILoggingEvent event) {
        String stackTrace = null;
        IThrowableProxy iThrowableProxy = event.getThrowableProxy();
        if (iThrowableProxy != null) {
            StringWriter writer = new StringWriter();
            PrintWriter pWriter = new PrintWriter(writer);
            ((ThrowableProxy) iThrowableProxy).getThrowable().printStackTrace(pWriter);
            stackTrace = replaceMessage(writer.getBuffer().toString());
        }
        return stackTrace;
    }

    private String cutFromEnd(String value) {
        if (cutNeeded) {
            if (value.length() > classNameSize) {
                value = value.substring(value.length() - classNameSize - 1);
            } else {
                value = StringUtils.rightPad(value, classNameSize);
            }
        }
        return value;
    }

    public void startTiming(String timingName) {
        timings.put(timingName, -1 * System.currentTimeMillis());
    }

    public void stopTiming(String timingName) {
        Long startTime = timings.get(timingName);
        if (startTime == null) {
            timings.put(timingName, -1L);
        } else {
            timings.put(timingName, System.currentTimeMillis() + startTime);
        }
    }

    public void startSummaryTiming(String timingName) {
        Long cnt = timings.get(timingName + ".count");
        if (cnt == null) {
            cnt = 0L;
        }
        timings.put(timingName + ".count", ++cnt);
        startTiming(timingName + ".summary#" + cnt);
    }

    public void stopSummaryTiming(String timingName) {
        Long cnt = timings.get(timingName + ".count");
        stopTiming(timingName + ".summary#" + cnt);
        Long summaryTime = timings.get(timingName + ".summary#" + cnt);
        if (timings.containsKey(timingName)) {
            summaryTime = summaryTime + timings.get(timingName);
        }
        timings.put(timingName, summaryTime);
        timings.remove(timingName + ".summary#" + cnt);
        timings.put(timingName + ".count", --cnt);
        if (cnt == 0) {
            timings.remove(timingName + ".count");
        }
    }

    public void stopAllTimers() {
        for (Object o : timings.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (((Long) pair.getValue() < 1L)) {
                stopTiming((String) pair.getKey());
                log.debug("Performance details: '" + pair.getKey() + " was stopped because it was not closed");
            }
        }
    }

    public void printTiming() {
        List<String> keys = new ArrayList<>(timings.getValuesInStackOrder());
        for (String timing : keys) {
            log.debug("Performance details: '" + timing + "':'" + timings.get(timing) + " ms'");
        }
    }

}
