package com.evbooksministry.bibleandbookministry.config;


import com.evbooksministry.bibleandbookministry.models.RequestLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncSaveUtil {
    private final RequestLogRepository logRepository;

    public AsyncSaveUtil(RequestLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Async
    protected void asyncSave(RequestLog log){
        logRepository.save(log);
    }
}
