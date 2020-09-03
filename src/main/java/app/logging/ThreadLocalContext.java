package app.logging;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThreadLocalContext {

    private static final ThreadLocal<ProcessingInfo> processingInfoThreadLocal = new ThreadLocal<>();

    public static ProcessingInfo getProcessingInfo() {
        if (processingInfoThreadLocal.get() == null) {
            processingInfoThreadLocal.set(new ProcessingInfo());
        }
        return processingInfoThreadLocal.get();
    }

    public static void drop() {
        processingInfoThreadLocal.set(null);
    }
}
